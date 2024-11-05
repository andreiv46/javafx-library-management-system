package org.ardeu.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.ardeu.librarymanagementsystem.domain.config.ServiceConfig;
import org.ardeu.librarymanagementsystem.domain.sampledata.SampleData;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.ui.components.SharedMenuBar;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.author.AddAuthorViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.author.AuthorsViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.book.AddBookViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.book.BookDetailsViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.book.BooksViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre.AddGenreViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre.GenreViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.home.HomeViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan.AddLoanViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan.LoansViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.member.AddMemberViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.member.MembersViewController;

import java.io.IOException;

public class LibraryApplication extends Application {

    private final ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) throws IOException {

        //loading the views
        FXMLLoader homeFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/home/home-view.fxml"));
        Pane homeView = homeFxmlLoader.load();

        FXMLLoader booksFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/book/books-view.fxml"));
        Pane booksView = booksFxmlLoader.load();

        FXMLLoader bookDetailsFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/book/book-details-view.fxml"));
        Pane bookDetailsView = bookDetailsFxmlLoader.load();

        FXMLLoader addBookFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/book/add-book-view.fxml"));
        Pane addBookView = addBookFxmlLoader.load();

        FXMLLoader editBookInfoFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/book/edit-book-information-view.fxml"));
        Pane editBookInfoView = editBookInfoFxmlLoader.load();

        FXMLLoader membersFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/member/members-view.fxml"));
        Pane membersView = membersFxmlLoader.load();

        FXMLLoader genresFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/genre/genres-view.fxml"));
        Pane genresView = genresFxmlLoader.load();

        FXMLLoader authorsFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/author/authors-view.fxml"));
        Pane authorsView = authorsFxmlLoader.load();

        FXMLLoader addAuthorFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/author/add-author-view.fxml"));
        Pane addAuthorView = addAuthorFxmlLoader.load();

        FXMLLoader addGenreFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/genre/add-genre-view.fxml"));
        Pane addGenreView = addGenreFxmlLoader.load();

        FXMLLoader addMemberFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/member/add-member-view.fxml"));
        Pane addMemberView = addMemberFxmlLoader.load();

        FXMLLoader loansFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/loan/loans-view.fxml"));
        Pane loansView = loansFxmlLoader.load();

        FXMLLoader addLoanFxmlLoader = new FXMLLoader(
                LibraryApplication.class.getResource("views/loan/add-loan-view.fxml"));
        Pane addLoanView = addLoanFxmlLoader.load();

        // initial view
        rootLayout = new BorderPane();
        rootLayout.setCenter(homeView);

        // main scene
        Scene scene = new Scene(rootLayout);
        stage.setTitle("Library Management System");
        stage.setScene(scene);

        // initialize the ScreenController
        ScreenViewController screenViewController = new ScreenViewController(rootLayout);
        screenViewController.addScreen(ScreenName.HOME, homeView, homeFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.BOOKS, booksView, booksFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.BOOK_DETAILS, bookDetailsView, bookDetailsFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.ADD_BOOK, addBookView, addBookFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.EDIT_BOOK_INFORMATION, editBookInfoView, editBookInfoFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.MEMBERS, membersView, membersFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.GENRES, genresView, genresFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.AUTHORS, authorsView, authorsFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.ADD_AUTHOR, addAuthorView, addAuthorFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.ADD_GENRE, addGenreView, addGenreFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.ADD_MEMBER, addMemberView, addMemberFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.LOANS, loansView, loansFxmlLoader.getController());
        screenViewController.addScreen(ScreenName.ADD_LOAN, addLoanView, addLoanFxmlLoader.getController());


        //screenController DI
        HomeViewController homeViewController = homeFxmlLoader.getController();
        homeViewController.setScreenViewController(screenViewController);

        BooksViewController bookViewController = booksFxmlLoader.getController();
        bookViewController.setScreenViewController(screenViewController);

        BookDetailsViewController bookDetailsViewController = bookDetailsFxmlLoader.getController();
        bookDetailsViewController.setScreenViewController(screenViewController);

        MembersViewController membersViewController = membersFxmlLoader.getController();
        membersViewController.setScreenViewController(screenViewController);

        GenreViewController genreViewController = genresFxmlLoader.getController();
        genreViewController.setScreenViewController(screenViewController);

        AuthorsViewController authorsViewController = authorsFxmlLoader.getController();
        authorsViewController.setScreenViewController(screenViewController);

        AddAuthorViewController addAuthorViewController = addAuthorFxmlLoader.getController();
        addAuthorViewController.setScreenViewController(screenViewController);

        AddGenreViewController addGenreViewController = addGenreFxmlLoader.getController();
        addGenreViewController.setScreenViewController(screenViewController);

        AddMemberViewController addMemberViewController = addMemberFxmlLoader.getController();
        addMemberViewController.setScreenViewController(screenViewController);

        AddBookViewController addBookViewController = addBookFxmlLoader.getController();
        addBookViewController.setScreenViewController(screenViewController);

        LoansViewController loansViewController = loansFxmlLoader.getController();
        loansViewController.setScreenViewController(screenViewController);

        AddLoanViewController addLoanViewController = addLoanFxmlLoader.getController();
        addLoanViewController.setScreenViewController(screenViewController);

        // Menu bar
        MenuBar menuBar = new SharedMenuBar(screenViewController);
        rootLayout.setTop(menuBar);

        //on close
        stage.setOnCloseRequest(_ -> {
            serviceRegistry.saveData();
        });

        stage.show();
    }

    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

        ServiceConfig serviceConfig = new ServiceConfig(serviceRegistry);
        serviceConfig.configureServices();

        serviceRegistry.loadData();

//        SampleData sampleData = new SampleData();
//        sampleData.resetFiles();
//        sampleData.populateData();
        launch();
    }
}