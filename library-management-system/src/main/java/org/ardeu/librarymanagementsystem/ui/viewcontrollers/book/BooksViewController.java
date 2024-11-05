package org.ardeu.librarymanagementsystem.ui.viewcontrollers.book;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

public class BooksViewController {

    private ScreenViewController screenViewController;
    private final BookController bookController;
    private final AuthorController authorController;
    private final GenreController genreController;
    private final ObservableMap<UUID, Book> books;
    private final ObservableList<Book> bookList;
    private final FilteredList<Book> filteredBookList;
    private final SortedList<Book> sortedBookList;

    @FXML
    public TableView<Book> booksTable;

    @FXML
    public MFXTextField titleInput;

    @FXML
    public MFXTextField authorInput;

    @FXML
    public MFXTextField genreInput;

    public BooksViewController() {
        bookController = new BookController();
        authorController = new AuthorController();
        genreController = new GenreController();

        books = bookController.getAllBooks().getData();
        bookList = FXCollections.observableArrayList(books.values().stream().toList());
        filteredBookList = new FilteredList<>(bookList, _ -> true);
        sortedBookList = new SortedList<>(filteredBookList);

        books.addListener((MapChangeListener<UUID, Book>) change -> {
            if (change.wasAdded()) {
                bookList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                bookList.remove(change.getValueRemoved());
            }
        });
    }

    @FXML
    public void initialize() {
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedBookList.comparatorProperty().bind(booksTable.comparatorProperty());
        setUpBooksTable(sortedBookList);

        titleInput.textProperty().addListener((_, _, _) -> filterBooks());
        authorInput.textProperty().addListener((_, _, _) -> filterBooks());
        genreInput.textProperty().addListener((_, _, _) -> filterBooks());

        booksTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && booksTable.getSelectionModel().getSelectedItem() != null) {
                Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
                openBookDetails(selectedBook);
            }
        });
    }

    private void openBookDetails(Book selectedBook) {
        BookDetailsViewController bookDetailsViewController =
                (BookDetailsViewController) screenViewController.getController(ScreenName.BOOK_DETAILS);
        bookDetailsViewController.setBook(selectedBook);
        screenViewController.activate(ScreenName.BOOK_DETAILS);
    }

    private void setUpBooksTable(ObservableList<Book> bookList){
        // id column
        TableColumn<Book, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        idColumn.setComparator(UUID::compareTo);
        idColumn.setSortable(true);

        // title column
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setComparator(String::compareTo);
        titleColumn.setSortable(true);


        // author column
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> {
            UUID authorId = cellData.getValue().getAuthorId();
            Result<Author> result = authorController.getAuthorById(authorId);
            if(result.isSuccess()){
                Author author = result.getData();
                return new SimpleStringProperty(author.getName());
            }
            return new SimpleStringProperty("not found");
        });
        authorColumn.setComparator(String::compareToIgnoreCase);
        authorColumn.setSortable(true);

        // date column
        TableColumn<Book, LocalDate> dateColumn = new TableColumn<>("Publication Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        dateColumn.setComparator(LocalDate::compareTo);
        dateColumn.setSortable(true);

        // genre column
        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(cellData -> {
            UUID genreId = cellData.getValue().getGenreId();
            Result<Genre> result = genreController.getGenreById(genreId);
            if(result.isSuccess()){
                Genre genre = result.getData();
                return new SimpleStringProperty(genre.getName());
            }
            return new SimpleStringProperty("not found");
        });
        genreColumn.setComparator(String::compareToIgnoreCase);
        genreColumn.setSortable(true);

        booksTable.setSortPolicy(_ -> true);
        booksTable.getColumns().addAll(idColumn, titleColumn, authorColumn, dateColumn, genreColumn);
        booksTable.setItems(bookList);
    }

    private void filterBooks(){
        filteredBookList.setPredicate(book -> {
            String title = titleInput.getText().toLowerCase(Locale.ROOT);
            String author = authorInput.getText().toLowerCase(Locale.ROOT);
            String genre = genreInput.getText().toLowerCase(Locale.ROOT);

            // author name
            Result<Author> authorResult = authorController.getAuthorById(book.getAuthorId());
            String authorName = "not found";
            if (authorResult.isSuccess()) {
                authorName = authorResult.getData().getName().toLowerCase(Locale.ROOT);
            }

            // genre name
            Result<Genre> genreResult = genreController.getGenreById(book.getGenreId());
            String genreName = "not found";
            if (genreResult.isSuccess()) {
                genreName = genreResult.getData().getName().toLowerCase(Locale.ROOT);
            }

            return book.getTitle().toLowerCase(Locale.ROOT).contains(title) &&
                    authorName.contains(author) &&
                    genreName.contains(genre);
        });
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }
}
