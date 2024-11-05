package org.ardeu.librarymanagementsystem.ui.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan.AddLoanViewController;

public class SharedMenuBar extends MenuBar {

    private final ScreenViewController screenViewController;

    public SharedMenuBar(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
        initializeMenu();
    }

    private void initializeMenu() {
        // books menu
        Menu booksMenu = new Menu("Books");
        MenuItem allBooksItem = new MenuItem("All books");
        MenuItem addBookItem = new MenuItem("Add book");
        booksMenu.getItems().addAll(allBooksItem, addBookItem);

        // members menu
        Menu membersMenu = new Menu("Members");
        MenuItem allMembersItem = new MenuItem("All members");
        MenuItem addMemberItem = new MenuItem("Add member");
        membersMenu.getItems().addAll(allMembersItem, addMemberItem);

        // genres menu
        Menu genresMenu = new Menu("Genres");
        MenuItem allGenresItem = new MenuItem("All genres");
        MenuItem addGenreItem = new MenuItem("Add genre");
        genresMenu.getItems().addAll(allGenresItem, addGenreItem);

        // authors menu
        Menu authorsMenu = new Menu("Authors");
        MenuItem allAuthorsItem = new MenuItem("All authors");
        MenuItem addAuthorItem = new MenuItem("Add author");
        authorsMenu.getItems().addAll(allAuthorsItem, addAuthorItem);

        // lans menu
        Menu loansMenu = new Menu("Loans");
        MenuItem allLoansItem = new MenuItem("All loans");
        MenuItem addLoanItem = new MenuItem("Add loan");
        loansMenu.getItems().addAll(allLoansItem, addLoanItem);

        // menu item actions
        allBooksItem.setOnAction(_ -> screenViewController.activate(ScreenName.BOOKS));
        addBookItem.setOnAction(_ -> screenViewController.activate(ScreenName.ADD_BOOK));
        allMembersItem.setOnAction(_ -> screenViewController.activate(ScreenName.MEMBERS));
        allGenresItem.setOnAction(_ -> screenViewController.activate(ScreenName.GENRES));
        allAuthorsItem.setOnAction(_ -> screenViewController.activate(ScreenName.AUTHORS));
        addAuthorItem.setOnAction(_ -> screenViewController.activate(ScreenName.ADD_AUTHOR));
        addGenreItem.setOnAction(_ -> screenViewController.activate(ScreenName.ADD_GENRE));
        addMemberItem.setOnAction(_ -> screenViewController.activate(ScreenName.ADD_MEMBER));
        allLoansItem.setOnAction(_ -> screenViewController.activate(ScreenName.LOANS));
        addLoanItem.setOnAction(_ -> {
            AddLoanViewController addLoanViewController = (AddLoanViewController) screenViewController.getController(ScreenName.ADD_LOAN);
            addLoanViewController.updateBooksList();
            screenViewController.activate(ScreenName.ADD_LOAN);
        });

        // add menus to the menu bar
        this.getMenus().addAll(booksMenu, genresMenu, authorsMenu, membersMenu, loansMenu);
    }
}