package com.metait.javafxwebpages;

import com.google.gson.Gson;
import com.metait.javafxwebpages.datarow.JSONWebAddress;
import com.metait.javafxwebpages.datarow.WebAddresItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is the main controller of the application.
 * This is setting a user agent value for webview controller.
 */
public class WebPagesController {
    @FXML
    private TextField textFieldSearch;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonMody;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonGlobalSearch;
    @FXML
    private Button buttonSearch;
    @FXML
    private TableView<WebAddresItem> tableViewWebPages;
    @FXML
    private TitledPane titledPaneWebPage;
    @FXML
    private SplitPane splitPanelWebPages;
    @FXML
    private Pane paneWebPages;
    @FXML
    private Label labelMsg;
    @FXML
    private TableColumn<WebAddresItem,Integer> tableColumnNr;
    @FXML
    private TableColumn<WebAddresItem,String>  tableColumnDate;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnStar;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnWebAddress;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnKeyWord;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnTitle;
    @FXML
    private TableColumn<WebAddresItem,Integer> tableColumnBookMk;
    @FXML
    private Label labelDate;
    @FXML
    private Label labelOrder;
    @FXML
    private ComboBox<String> comboStar;
    @FXML
    private TextField textFieldKeyWord;
    @FXML
    private HBox hboxEdit;
    @FXML
    private TextField textFieldWebAddress;
    @FXML
    private Button buttonSave;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private RadioButton radioButtonNr;
    @FXML
    private RadioButton radioButtonDate;
    @FXML
    private RadioButton radioButtonStar;
    @FXML
    private RadioButton radioButtonKeyWord;
    @FXML
    private RadioButton radioButtonTitle;
    @FXML
    private RadioButton radioButtonWebAddress;
    @FXML
    private WebView webView;
    @FXML
    private Button buttonOpenBrowser;
    @FXML
    private RadioButton radioButtonGlobal;
    @FXML
    private ToggleButton buttonListAll;
    @FXML
    private ToggleButton buttonBookMark;
    @FXML
    private RadioButton radioButtonBookMark;
    @FXML
    private TextField textFieldShow;
    @FXML
    private CheckBox checkBoxInCaseSearch;
    @FXML
    private Button buttonNewRow;
    @FXML
    private FlowPane flowPaneEdit;
    @FXML
    private Button buttonCopyIntoClipBoard;
    /*
    @FXML
    private Label headerNumbere;
    @FXML
    private Label headerDate;
    @FXML
    private Label headerKeyWords;
    @FXML
    private Label headerWebAddress;
    @FXML
    private Label headerStar;
    */

    private int iStartRow = 1;
    private ObservableList<WebAddresItem> webAddressRows = FXCollections.<WebAddresItem>observableArrayList();
    private ChangeListener<Boolean> radioButtonChangeListener = null;
    public static final String cnstNr = "Nr";
    public static final String cnstStar = "Star";
    public static final String cnstDate = "Date";
    public static final String cnstKeyword = "Keyword";
    public static final String cnstTitle = "Title";
    public static final String cnstWebAddress = "WebAddress";

    public static enum COLUMNHEADERS { cnstNr, cnstStar, cnstDate, cnstKeyword, cnstTitle, cnstWebAddress };
    private COLUMNHEADERS columnName;
    private String strSearch;
    private static String java_user_home = System.getProperty("user.home");
    public static final String cnstUserDirOfApp = ".javawebaddressfx";
    private Stage primaryStage;
    private JsonConfig jsonConfig;
    //  private  KeyCombination pasteKeyCombination = new KeyCodeCombination(KeyCode.V,KeyCombination.CONTROL_DOWN);
    private boolean bSearchWithLowerCaseStrings = false;
    private EventHandler<KeyEvent> keyEventHanler = null;
    private String strWebViewDocTitle = null;
    private String strWebViewDocKeyWord = null;
    private WebAddresItem jusAddedWebAddresItem = null;
    private boolean bPressedButtonNewRow = false;
    private String cnstUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0";
    private TextField selectedRowTextField;

    private ChangeListener<Boolean> focuslistener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
        {
            if (newValue.booleanValue()) {
                focusGainedOfControl(observable);
            } else {
                focusLostOfControl(observable);
            }
        }
    };

    private void focusGainedOfControl(ObservableValue stackPane)
    {
        /*
        System.out.println("focus gained ObservableValue " +stackPane.getClass().getName());
        System.out.println("focus gained ObservableValue " +stackPane.getValue());
         */
        if (textFieldWebAddress.isFocused()) {
            selectedRowTextField = textFieldWebAddress;
            textFieldShow.setText(textFieldWebAddress.getText());
        }
        else
        // System.out.println("textFieldKeyWord=" +textFieldKeyWord.isFocused());
        if (textFieldKeyWord.isFocused()) {
            selectedRowTextField = textFieldKeyWord;
            textFieldShow.setText(textFieldKeyWord.getText());
        }
        else
        //System.out.println("textFieldTitle=" +textFieldTitle.isFocused());
        if (textFieldTitle.isFocused()) {
            selectedRowTextField = textFieldTitle;
            textFieldShow.setText(textFieldTitle.getText());
        }
    }

    @FXML
    private void pressedButtonCopyIntoClipBoard()
    {
        if (selectedRowTextField != null)
        {
            Clipboard cb = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(selectedRowTextField.getText());
            cb.setContent(content);
        }
    }
    private void focusLostOfControl(ObservableValue stackPane)
    {
        /*
        System.out.println("focus lost ObservableValue " +stackPane.getClass().getName());
        System.out.println("focus lost ObservableValue " +stackPane.getValue());
         */
    }

    public void handleKeyEvent(KeyEvent event) {
        keyEventHanler.handle(event);
    }

    public void pressedButtonDelete() {
       // System.out.println("pressedButtonDelete");
        WebAddresItem webAddresItem = tableViewWebPages.getSelectionModel().getSelectedItem();

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "" +(webAddresItem.getTitle() != null ? webAddresItem.getTitle() :
                        webAddresItem.getWebaddress()) +"\n" +
                "Should this row will be deleted?", okButtonType, cancelType);
        alert.setTitle("Delete row");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-weight: bold");
        Optional<ButtonType> result = alert.showAndWait();

        if (webAddresItem != null)
        if (result.orElse(cancelType) == okButtonType) {
            webAddressRows.remove(webAddresItem);
            saveWebAddressItems();
        }
    }

    @FXML
    protected void pressedCheckBoxInCaseSearch()
    {
        if (checkBoxInCaseSearch.isSelected())
            bSearchWithLowerCaseStrings = true;
        else
            bSearchWithLowerCaseStrings = false;
    }
    @FXML
    protected void pressedButtonGlobalSearch()
    {
        if (textFieldSearch.getText().trim().length()==0)
        {
            tableViewWebPages.setItems(webAddressRows);
            buttonListAll.setSelected(true);
            return;
        }

        buttonListAll.setSelected(false);
        // System.out.println("pressedButtonGlobalSearch");
        // to filter
        final boolean bToLowerCaseLetters = bSearchWithLowerCaseStrings;
        String strAtFirstSearch = textFieldSearch.getText();
        if (bToLowerCaseLetters && strAtFirstSearch != null)
            strAtFirstSearch = strAtFirstSearch.toLowerCase();
        final String strSearch = strAtFirstSearch;

        FilteredList<WebAddresItem> listFiltered = webAddressRows.filtered(
                new Predicate<WebAddresItem>(){
                    public boolean test(WebAddresItem t){
                        String strValue = t.toString();
                        if (strValue == null)
                            return false;
                        if (bToLowerCaseLetters)
                            strValue = strValue.toLowerCase();
                        return strValue.contains(strSearch);
                    }
                });
        tableViewWebPages.setItems(listFiltered);
    }

    @FXML
    protected void pressedButtonSearch()
    {
    //    System.out.println("pressedButtonSearch");
        /*
        if (textFieldSearch.getText().trim().length()==0)
        {
            tableViewWebPages.setItems(webAddressRows);
            buttonListAll.setSelected(true);
            return;
        }
         */

        FilteredList<WebAddresItem> listFiltered = null;
        // System.out.println("pressedButtonGlobalSearch");
        // to filter

        /*
        break;
        case "cnstDate":
        ret = item.dateProperty().toString().contains(search);
        break;
        case "cnstWebAddress":
        ret = item.webaddressProperty().toString().contains(search);
        break;
        case "cnstKeyword":
        ret = item.keywordProperty().toString().contains(search);
        break;
        case "cnstStar":
        ret = item.starProperty().toString().contains(search);
        break;
        case "cnstTitle":
        ret = item.titleProperty().toString().contains(search);
        break;
        */

        buttonListAll.setSelected(false);

        final boolean bToLowerCaseLetters = bSearchWithLowerCaseStrings;
        String strAtFirstSearch = textFieldSearch.getText();
        if (bToLowerCaseLetters && strAtFirstSearch != null)
            strAtFirstSearch = strAtFirstSearch.toLowerCase();
        final String strSearch = strAtFirstSearch;

        columnName = COLUMNHEADERS.cnstWebAddress;
        if (radioButtonNr.isSelected()) {
            columnName = COLUMNHEADERS.cnstWebAddress;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                           return Integer.valueOf(t.getOrder()).toString().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonTitle.isSelected()) {
            columnName = COLUMNHEADERS.cnstTitle;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            String strValue = t.getTitle();
                            if (strValue == null)
                                return false;
                            if (bToLowerCaseLetters)
                                strValue = strValue.toLowerCase();
                            return strValue.contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonStar.isSelected()) {
            columnName = COLUMNHEADERS.cnstStar;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            if (strSearch == null || strSearch.trim().length()==0)
                                return t.getStar() == null || t.getStar().isBlank() || t.getStar().isEmpty();
                            return t.getStar() != null && t.getStar().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonWebAddress.isSelected()) {
            columnName = COLUMNHEADERS.cnstNr;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            String strValue = t.getWebaddress();
                            if (strValue == null)
                                return false;
                            if (bToLowerCaseLetters)
                                strValue = strValue.toLowerCase();
                            return strValue.contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonDate.isSelected()) {
            columnName = COLUMNHEADERS.cnstDate;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            String strValue = t.getDate();
                            if (strValue == null)
                                return false;
                            if (bToLowerCaseLetters)
                                strValue = strValue.toLowerCase();
                            return strValue.contains(strSearch);
                        }
                    });
        }
       else
        if (radioButtonKeyWord.isSelected()) {
            columnName = COLUMNHEADERS.cnstDate;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            String strValue = t.getKeyword();
                            if (strValue == null)
                                return false;
                            if (bToLowerCaseLetters)
                                strValue = strValue.toLowerCase();
                            return strValue.contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonBookMark.isSelected()) {
            columnName = COLUMNHEADERS.cnstDate;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            return Integer.valueOf(t.getBookmark()).toString().contains(strSearch);
                        }
                    });
        }

        /*
        FilteredList<WebAddresItem> listFiltered = webAddressRows.filtered(
                new Predicate<WebAddresItem>(){
                    public boolean test(WebAddresItem t){
                        return columnContains(columnName, strSearch, t);
                    }
                });
         */
        tableViewWebPages.setItems(listFiltered);
    }


    private boolean columnContains(COLUMNHEADERS columnName,
                                   String search, WebAddresItem item)
    {
        boolean ret = false;
        if (columnName != null && columnName.toString().trim().length()>0
                && search != null && search.trim().length()>0)
        {
            System.out.println("'" +columnName +"'");
            switch (columnName.toString())
            {
                case "cnstNr":
                    ret = item.orderProperty().toString().contains(search);
                    break;
                case "cnstDate":
                    ret = item.dateProperty().toString().contains(search);
                    break;
                case "cnstWebAddress":
                    ret = item.webaddressProperty().toString().contains(search);
                    break;
                case "cnstKeyword":
                    ret = item.keywordProperty().toString().contains(search);
                    break;
                case "cnstStar":
                    ret = item.starProperty().toString().contains(search);
                    break;
                case "cnstTitle":
                    ret = item.titleProperty().toString().contains(search);
                    break;
            }
        }
        return ret;
    }

    public void setPrimaryStage(Stage stage)
    {
        primaryStage = stage;
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                appIsClosing();
            }
        });
    }

    private void appIsClosing()
    {
        // TODO:
        System.out.println("loppu");
        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
        saveSelectedItemPosition(item);
        saveWebAddressItems();
        Platform.exit();
        System.exit(0);
    }

    private File getSelectedWebAddressFile()
    {
        File appDir = getAppDir();
        if (appDir == null)
            return null;
        File selectedFile = new File(appDir +File.separator +"selectedwebaddress.json");
        return selectedFile;
    }

    private void saveSelectedItemPosition(WebAddresItem item)
    {
        File selectedwebaddressFiler = getSelectedWebAddressFile();
        if (selectedwebaddressFiler == null)
            return;
        Writer writer = null;
        try {
            Path path = Paths.get(selectedwebaddressFiler.getAbsolutePath());
            StandardOpenOption writeOption = StandardOpenOption.TRUNCATE_EXISTING;
            if (!selectedwebaddressFiler.exists())
                writeOption = StandardOpenOption.CREATE;
            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, writeOption);
            Gson gson = new Gson();
            List<JSONWebAddress> jsonList = new ArrayList<>();
            if (jsonConfig == null)
                jsonConfig = new JsonConfig();
            if (item != null)
                jsonConfig.setLastOrderNumber(item.getOrder());
            gson.toJson(jsonConfig, writer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            labelMsg.setText("Write file " + selectedwebaddressFiler.getAbsolutePath() + " error: " + ioe.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                    labelMsg.setText("Closing file " + selectedwebaddressFiler.getAbsolutePath() + " error: " + ioe2.getMessage());
                }
            }
        }
    }

    @FXML
    public void initialize() {

        webView.setContextMenuEnabled(true);

        buttonDelete.setDisable(true);
        buttonAdd.setDisable(true);

        Tooltip tableTip = new Tooltip(
                "Paste web address into table or\nDouble click a table row to show the page below web component");
        tableTip.setStyle("-fx-font-weight: bold; -fx-text-fill: yellow; -fx-font-size: 14");
        tableTip.setShowDelay(Duration.seconds(4));
        tableViewWebPages.setTooltip(tableTip);

        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(new WebPageLoadListener(webView, this));

        buttonListAll.setSelected(true);

        textFieldWebAddress.focusedProperty().addListener(focuslistener);
        textFieldKeyWord.focusedProperty().addListener(focuslistener);
        textFieldTitle.focusedProperty().addListener(focuslistener);

        textFieldWebAddress.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (newValue != null && newValue.trim().length() > 0) {
                    if (isStringWebAddressOK(newValue))
                    {
                        buttonAdd.setDisable(false);
                    }
                }
                else
                    buttonAdd.setDisable(true);
            }
        });

        radioButtonChangeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (radioButtonGlobal.isSelected()) {
                    buttonGlobalSearch.setDisable(false);
                    buttonSearch.setDisable(true);
                }
                else
                if (newValue) {
                    buttonGlobalSearch.setDisable(true);
                    buttonSearch.setDisable(false);
                }
            }
        };

        radioButtonGlobal.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonDate.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonNr.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonKeyWord.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonStar.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonTitle.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonWebAddress.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonBookMark.selectedProperty().addListener(radioButtonChangeListener);

        buttonGlobalSearch.setDisable(false);
        buttonSearch.setDisable(true);

        /*
        labelSearchDate.setFocusTraversable(true);
        labelSearchStar.setFocusTraversable(true);
        labelSearchKeyWord.setFocusTraversable(true);
        labelSearchTitle.setFocusTraversable(true);
         */

        textFieldShow.setStyle("-fx-font-weight: bold");
        tableViewWebPages.setStyle("-fx-font-weight: bold");
        textFieldTitle.setStyle("-fx-font-weight: bold");
        textFieldKeyWord.setStyle("-fx-font-weight: bold");
        textFieldWebAddress.setStyle("-fx-font-weight: bold");
        labelOrder.setStyle("-fx-font-weight: bold");
        labelDate.setStyle("-fx-font-weight: bold");
        textFieldSearch.setStyle("-fx-font-weight: bold");

        tableColumnNr.setCellValueFactory(new PropertyValueFactory("order"));
        tableColumnNr.setStyle( "-fx-alignment: CENTER-RIGHT;");
        tableColumnDate.setCellValueFactory(new PropertyValueFactory("date"));
        tableColumnDate.setStyle( "-fx-alignment: CENTER-RIGHT;");

        tableColumnStar.setCellValueFactory(new PropertyValueFactory("star"));
        tableColumnStar.setStyle( "-fx-alignment: CENTER-RIGHT;");
        tableColumnWebAddress.setCellValueFactory(new PropertyValueFactory("webaddress"));
        tableColumnKeyWord.setCellValueFactory(new PropertyValueFactory("keyword"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory("title"));
        tableColumnBookMk.setCellValueFactory(new PropertyValueFactory("bookmark"));
        tableColumnBookMk.setStyle( "-fx-alignment: CENTER;");
        /*
        tableViewWebPages.setStyle(".table-row-cell:selected .tissue-cell {\n" +
                "    -fx-background-color: #F5AD11;\n" +
                "    -fx-text-fill: white;\n" +
                "}");
        */
        /*lor: steelblue;\n" +
                "            -fx-text-background-color: red;\n" +
                "        }\n");
         */

        tableViewWebPages.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
               editSelectedWebAddress(newSelection);
            }
        });

        tableViewWebPages.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                      // System.out.println("Double clicked");
                        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
                        if (item != null)
                            loadWebPageIntoWebView(item);
                    }
                }
            }
        });

       //  tableViewWebPages.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
     // tableViewWebPages.getSelectionModel().setCellSelectionEnabled(true);

        tableViewWebPages.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.getTableColumn() != null){
              //  tableViewWebPages.getSelectionModel().selectRange(0, newVal.getTableColumn(), tableViewWebPages.getItems().size(), newVal.getTableColumn());
                /*
                System.out.println("Selected TableColumn: "+ newVal.getTableColumn().getText());
                System.out.println("Selected column index: "+ newVal.getColumn());
                 */
            }
        });

        // tableColumnWebAddress.setStyle( "-fx-alignment: CENTER-RIGHT;");

        comboStar.getItems().addAll(
                "*",
                "**",
                "***",
                "****",
                "*****"
        );

        tableViewWebPages.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */
                Clipboard cb = Clipboard.getSystemClipboard();
                boolean success = false;
                if (cb.hasString()) {
                    /*
                    System.out.println("Dropped: " +cb.getString());
                    System.out.println("target: " + event.getTarget().toString());
                     */
                    handlePossibleUrl(cb.getString());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

       //
        // gridPaneWebPages.onKeyPressedProperty(pasteKeyCombination);
        keyEventHanler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.V && event.isControlDown()) {
                    Clipboard clipboard = Dragboard.getSystemClipboard();
                    boolean success = false;
                    if (clipboard.hasString()) {
                        System.out.println("clibboard: " + clipboard.getString());
                        System.out.println("target: " + event.getTarget());
                        Control control = (Control)event.getTarget();
                        System.out.println("id: " + control.getId());
                        handlePossibleUrl(clipboard.getString());
                        success = true;
                    }
                    event.consume();
                }
            }
        };

        // scene.setOnKeyPressed(keyEventHanler);

        /*
        paneWebPages.setOnKeyPressed(keyEventHanler);
        paneWebPages.setFocusTraversable(true);
        gridPaneWebPages.setOnKeyPressed(keyEventHanler);
        gridPaneWebPages.setFocusTraversable(true);
        titledPaneWebPage.setOnKeyPressed(keyEventHanler);
        splitPanelWebPages.setOnKeyPressed(keyEventHanler);
        titledPaneWebPage.setFocusTraversable(true);
         */
        tableViewWebPages.setItems(webAddressRows);
        initGridPane();
        readWebAddressListFromFile();
        readJsonConfigFile();
    }

    private void loadWebPageIntoWebView(WebAddresItem item)
    {
        if (item == null)
            return;
        String webAddress = item.getWebaddress();
        WebEngine engine = webView.getEngine();
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    jusAddedWebAddresItem = item;
                    String modUrl = getCorrectedUlr(webAddress);
                    if (modUrl != null) {
                        if (!modUrl.equals(webAddress)) {
                            item.setWebaddress(modUrl);
                            saveWebAddressItems();
                        }
                        engine.load(modUrl);
                    }
                }catch (Exception e){
                    labelMsg.setText("Error: " +e.getMessage());
                    engine.load(null);
                }
            }
        });
    }

    private String getCorrectedUlr(String webaddress)
    {
        String ret = webaddress;
        if (ret != null && !ret.startsWith("http://")
            && !ret.startsWith("https://") && !ret.startsWith("fps:")
            && !ret.startsWith("fp:"))
        {
            ret = "http://" +ret;
        }
        return ret;
    }
    private void editSelectedWebAddress(WebAddresItem newSelection)
    {
        textFieldShow.setText("");
        bPressedButtonNewRow = false;

        buttonDelete.setDisable(false);
        buttonSave.setDisable(false);
        buttonOpenBrowser.setDisable(false);

        buttonNewRow.setDisable(false);
        labelDate.setText(""+newSelection.getDate());
        labelOrder.setText(""+newSelection.getOrder());
        textFieldKeyWord.setText(""+newSelection.getKeyword());
        String start = newSelection.getStar();
        boolean bNullValue = false;
        if (start == null || start.trim().length()==0)
            bNullValue = true;
        if (!bNullValue) {
            int iStar = start.trim().length();
            if (iStar > 0)
                comboStar.getSelectionModel().select(iStar - 1);
            else
                comboStar.setValue(null);
        }
        else
            comboStar.setValue(null);
        textFieldTitle.setText(newSelection.getTitle());
        textFieldWebAddress.setText(""+newSelection.getWebaddress());
        buttonBookMark.setSelected(newSelection.getBookmark()!=0);
        buttonDelete.setDisable(false);
    }

    private void initGridPane()
    {
        /*
        int iOrder = 1;
        int iStars = 5;
        String keyWords = "keyworda1, keyworda2";
        String webAddress = "http://webaddress.com";
        String tile = "tile1, tile2";

      //  WebAddressRow row1 = new WebAddressRow(iOrder, iStars, keyWords, webAddress);
        // int iNr, int iSTar, String strDAte, String webAddress
        WebAddresItem item = new WebAddresItem(iOrder, iStars, getTodayString(), keyWords, webAddress, tile);
        int max = 6;
        String itemKWord, title;
        for(int i = 1; i < max; i++)
        {
            iOrder = i;
            iStars = i;
            itemKWord = "keyworda1_" +i +", " +"keyworda2_" +i;
            webAddress = "http://webaddress.com_" +i +", " +"http://webaddress.com_" +i;
            title = "title1_" +i +", " +"title2_" +i;
            item = new WebAddresItem(iOrder, iStars, getTodayString(), itemKWord, webAddress, title);
            webAddressRows.add(item);
        }
        // addGridPaneRow(row1);
         */
    }

    private String getTodayString()
    {
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    @FXML
    private void pressedButtonHelp()
    {
        System.out.println("pressedButtonHelp");
    }

    private void handlePossibleUrl(String fromClibBoard)
    {
        if (fromClibBoard == null || fromClibBoard.trim().length()==0)
            return;
        String tryUrl = fromClibBoard;
        try {
            try {
                new URL(tryUrl);
            }catch (MalformedURLException mfe){
                tryUrl = "https://" +fromClibBoard;
                try {
                    new URL(tryUrl);
                }catch (MalformedURLException mfe2){
                    tryUrl = "http://" +fromClibBoard;
                    try {
                        new URL(tryUrl);
                    }catch (MalformedURLException mfe3){
                        if (File.separatorChar == '\\')
                            tryUrl = "file:///" +fromClibBoard;
                        else
                            tryUrl = "file://" +fromClibBoard;
                        try {
                            new URL(tryUrl);
                        }catch (MalformedURLException mfe4) {
                            throw mfe4;
                        }
                    }
                }
            }
            addURLIntoGridWebPages(tryUrl);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            String msg = "Text paste is not web address: " +malformedURLException.getMessage();
            System.out.println(msg);
            Platform.runLater(new Runnable() {
                public void run() {
                    labelMsg.setText(msg);
                }
            });
            return;
        }
    }

    private boolean existingURLINwebAddressRows(String weburl)
    {
        boolean ret = false;
        if (weburl == null || weburl.trim().length()==0)
            ret = true;
        else
        {
            String webAddress = null;
            for (WebAddresItem item : webAddressRows)
            {
                if (item == null)
                    continue;
                webAddress = item.getWebaddress();
                if (webAddress != null && webAddress.equals(weburl)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    private int getMaxOrderValueFromRows()
    {
        int ret = 0;
        for(WebAddresItem item : webAddressRows)
        {
            if (item == null)
                continue;
            if (item.getOrder() > ret)
                ret = item.getOrder();
        }
        return ret +1;
    }
    private void addURLIntoGridWebPages(String fromClibBoard)
    {
        if (existingURLINwebAddressRows(fromClibBoard))
        {
            labelMsg.setText("The same ulr is existing allready! " +fromClibBoard);
            return;
        }

        int iOrder = webAddressRows.size() +1;
        if (iOrder > 1)
            iOrder = getMaxOrderValueFromRows();
        int iStars = 0;
        String itemKWord = "";
        String strWebPage = null;
        String title = "";
        try {
            WebEngine webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);
            // webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Trident/7.0; rv:11.0) like Gecko");
            webEngine.setUserAgent(cnstUserAgent);
            // webEngine.load(fromClibBoard);
            /*
            File f = new File("C:\\Java\\project\\javafx\\javafxplayer\\src\\main\\resources\\com\\metait\\javafxplayer\\help\\help.html");
            if (f.exists())
             */
            final String modUrl = getCorrectedUlr(fromClibBoard);
            WebAddresItem item = new WebAddresItem(iOrder, iStars, getTodayString(), itemKWord, modUrl, title, 0);
            jusAddedWebAddresItem = item;
            webAddressRows.add(item);

            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        System.out.println("webengine->");
                        // webEngine.load(f.toURI().toString());
                        webEngine.setUserAgent(cnstUserAgent);
                        webEngine.load(modUrl);
                    }catch (Exception e2){
                        System.out.println("webengine-> e2");
                        e2.printStackTrace();
                        labelMsg.setText(e2.getMessage());
                    }
                }
            });

            /*
            Url2String url2String = new Url2String();
            // webEngine.loadContent(strWebPage);
            URL url = null;
            try {
                url = new URL(fromClibBoard);
                strWebPage = url2String.getStringFromUrl(url);
                String unknowCharacterSetName = url2String.getUnknowCharacterSetName();
                if (unknowCharacterSetName != null)
                    System.err.println("Possible error in url2String: " +unknowCharacterSetName);
                int iConfidence = url2String.getiConfidence();
                if (iConfidence != -1)
                    System.err.println("iConfidence in url2String: " +iConfidence);
            }catch (Exception e){
                // url = new URL(null, fromClibBoard, new sun.net.www.protocol.https.Handler());
                try {
                url = new URL(fromClibBoard);
                strWebPage = url2String.getStringFromUrl(url.toURI().toURL());
                }catch (Exception e3){
                    if (fromClibBoard.startsWith("file:")) {
                        String tryUrl = fromClibBoard.replace("file:///","").replace("file://","");
                        File f2 = new File(tryUrl);
                        if (f2.exists()) {
                            url = f2.toURI().toURL();
                            strWebPage = url2String.inputStreamToString(new FileInputStream(f2));
                        }
                        else
                            return;
                    }
                }
            }
            itemKWord = getKeyWordsFromWebPage(strWebPage);
            title = getTitleFromWebPage(strWebPage);
             */
        }catch (Exception e){
            e.printStackTrace();
            labelMsg.setText("Error: " +e.getMessage());
        }
        /*
        WebAddresItem item = new WebAddresItem(iOrder, iStars, getTodayString(), itemKWord, fromClibBoard, title, 0);
        jusAddedWebAddresItem = item;
        webAddressRows.add(item);
        */
    }

    public void calledWhenWebViewDocLoaded(String title, String keyword)
    {
        strWebViewDocTitle = title;
        strWebViewDocKeyWord = keyword;
        if (jusAddedWebAddresItem != null) {
            if (!(jusAddedWebAddresItem.getTitle() != null
                && title == null))
            jusAddedWebAddresItem.setTitle(strWebViewDocTitle);
            if (!(jusAddedWebAddresItem.getKeyword() != null
                    && keyword == null))
            jusAddedWebAddresItem.setKeyword(keyword);
            saveWebAddressItems();
        }
    }

    private String getTitleFromWebPage(String strWebPage)
    {
        String ret = "";
        boolean bTitleFounded = false;
        if (strWebPage != null && strWebPage.trim().length()>0)
        {
            ret = getTextXmlBlockOf("title", strWebPage);
            if (ret == null || ret.trim().length()==0)
                ret = getTextXmlBlockOf("TITLE", strWebPage);
            if (ret == null || ret.trim().length()==0)
            {
                ret = getTextXmlBlockOf("h1", strWebPage);
                if (ret == null || ret.trim().length()==0)
                    ret = getTextXmlBlockOf("H1", strWebPage);
                if (ret == null || ret.trim().length()==0)
                    ret = getTextXmlBlockOf("h2", strWebPage);
                if (ret == null || ret.trim().length()==0)
                    ret = getTextXmlBlockOf("H2", strWebPage);
            }
            if (ret != null && ret.contains("<") && ret.contains(">"))
                ret = ret.replaceAll("\\<.*?\\>", "")
                        .replaceAll("\\</.*?\\>", "");
            if (ret != null)
                ret = ret.replaceAll("\n"," ").replaceAll("\\s+"," ");
        }
        return ret;
    }

    private String getTextXmlBlockOf(String h1, String strWebPage)
    {
        String ret = ";";
        boolean bTitleFounded = false;
        Pattern pattern = Pattern.compile("<" +h1 +"\\s*.*?>");
        Matcher matcher = pattern.matcher(strWebPage);
        if (matcher.find())
        {
            int iStart = matcher.start();
            int iEnd = matcher.end();
            String strRest = strWebPage.substring(iEnd);
            int ind = strRest.indexOf("</" +h1 +">");
            if (ind > -1) {
                String value = strRest.substring(0, ind);
                if (value != null && value.contains("<") && value.contains(">"))
                    value = value.replaceAll("\\<.*?\\>", "")
                            .replaceAll("\\</.*?\\>", "");
                if (value != null)
                    value = value.replaceAll("\n"," ").replaceAll("\\s+"," ");
                ret = value;
            }
        }
        return ret;
    }

    private String getKeywordsFromContent(String strContent)
    {
        if (strContent == null || strContent.trim().length()==0)
            return "";
        Pattern pattern2 = Pattern.compile("content\\s*=\\s*\"(.*)\"");
        Matcher matcher2 = pattern2.matcher(strContent);
        if (matcher2.find())
        {
            String value1 = matcher2.group(1);
            return value1;
        }
        return "";
    }

    private String getKeyWordsFromWebPage(String strWebPage)
    {
        String ret = "";
        if (strWebPage != null && strWebPage.trim().length()>0)
        {
            Pattern pattern = Pattern.compile("<meta\\s+(.*?)name\\s*=\\s*\"keywords\"(.*?)/*>");
            Matcher matcher = pattern.matcher(strWebPage);
            if (matcher.find())
            {
                String value1 = matcher.group(1);
                String value2 = matcher.group(2);
                int ind1 = -1, ind2 = -1;
                if (value1 != null)
                {
                    ind1 = value1.indexOf("content");
                    if (ind1 > -1)
                    {
                        ret = getKeywordsFromContent(value1);
                    }
                }
                if (ind1 == -1 && value2 != null)
                {
                    ind2 = value2.indexOf("content");
                    if (ind2 > -1)
                    {
                        ret = getKeywordsFromContent(value2);
                    }
                }

                if (ret != null && ret.contains("<") && ret.contains(">"))
                    ret = ret.replaceAll("\\<.*?\\>", "")
                            .replaceAll("\\</.*?\\>", "");
                if (ret != null)
                    ret = ret.replaceAll("\n"," ").replaceAll("\\s+"," ");
            }
        }
        return ret;
    }

    private File getAppDir()
    {
        String userHome = java_user_home;
        File userDir = new File(userHome);
        if (!userDir.exists())
            userDir = new File(".");
        File appDir = new File(userDir +File.separator +cnstUserDirOfApp);
        if (!appDir.exists())
            if (!appDir.mkdir())
            {
                labelMsg.setText("Cannot create dir: " +appDir.getAbsolutePath());
                return null;
            }
        return appDir;
    }

    private File getJsonRowFile()
    {
        File rowFile = null;
        File appDir = getAppDir();
        if (appDir == null)
            return null;
        rowFile = new File(appDir +File.separator +"webaddressrows.json");
        return rowFile;
    }

    private void selectWebAddressItemInTableView(WebAddresItem item)
    {
        if (item == null)
            return;
        final WebAddresItem selectingItem = item;
        Platform.runLater(new Runnable() {
            public void run() {
                tableViewWebPages.requestFocus();
                tableViewWebPages.getSelectionModel().select(selectingItem);
                int iSelected = tableViewWebPages.getSelectionModel().getFocusedIndex();
                if (iSelected != -1) {
                    tableViewWebPages.getFocusModel().focus(iSelected);
                    tableViewWebPages.scrollTo(iSelected);
                                    /* under swing time:
                                    int rowIndex = iSelected;
                                    int columnIndex = 0;
                                    boolean includeSpacing = true;
                                    Rectangle cellRect = tableViewWebPages.getCellRect(rowIndex, columnIndex, includeSpacing);
                                    tableViewWebPages.scrollRectToVisible(cellRect);
                                     */
                }
            }
        });
    }

    private void readJsonConfigFile()
    {
        File selectedwebaddressFiler = getSelectedWebAddressFile();
        if (selectedwebaddressFiler == null)
            return;
        if (!selectedwebaddressFiler.exists())
            return;
        if (selectedwebaddressFiler.length() == 0)
            return;
        Reader reader = null;
        try {
            Path path = Paths.get(selectedwebaddressFiler.getAbsolutePath());
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            jsonConfig = gson.fromJson(reader, JsonConfig.class);
            if (jsonConfig.getLastOrderNumber() > -1 && webAddressRows != null
                && webAddressRows.size()>0)
            {
                int iSelect = jsonConfig.getLastOrderNumber();
                final WebAddresItem selectingItem;
                for (WebAddresItem item : webAddressRows)
                {
                    if (item.getOrder() == iSelect) {
                        selectingItem = item;
                        selectWebAddressItemInTableView(selectingItem);
                        break;
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            labelMsg.setText("Read file " + selectedwebaddressFiler.getAbsolutePath() + " error: " + ioe.getMessage());
        } catch (Exception norm_e) {
            norm_e.printStackTrace();
            labelMsg.setText("Read file " + selectedwebaddressFiler.getAbsolutePath() + " error: " + norm_e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                    labelMsg.setText("Closing file " + selectedwebaddressFiler.getAbsolutePath() + " error: " + ioe2.getMessage());
                }
            }
        }
    }

    private void readWebAddressListFromFile()
    {
        File rowFile = getJsonRowFile();
        if (rowFile == null)
            return;
        if (!rowFile.exists())
            return;
        if (rowFile.length() == 0)
            return;
        Reader reader = null;
        try {
            Path path = Paths.get(rowFile.getAbsolutePath());
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            JSONWebAddress[] jsonWebAddresItems = gson.fromJson(reader, JSONWebAddress[].class);
            for(JSONWebAddress jsonitem: jsonWebAddresItems)
            {
                webAddressRows.add(new WebAddresItem(jsonitem));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            labelMsg.setText("Read file " + rowFile.getAbsolutePath() + " error: " + ioe.getMessage());
        } catch (Exception norm_e) {
            norm_e.printStackTrace();
            labelMsg.setText("Read file " + rowFile.getAbsolutePath() + " error: " + norm_e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                    labelMsg.setText("Closing file " + rowFile.getAbsolutePath() + " error: " + ioe2.getMessage());
                }
            }
        }
    }

    private void saveWebAddressItems() {
     //   StringBuffer sb = new StringBuffer();
        Gson gson = null;
        String json = null;
        gson = new Gson();
        /*
        JSONHolderFowWebAddressList holder = new JSONHolderFowWebAddressList();
        for (WebAddresItem item : webAddressRows) {
            // json = gson.toJson(item);
            holder.webAddresItems.add(item);
            // sb.append(json);
        }
         */

        File rowFile = getJsonRowFile();
        if (rowFile == null)
            return;
        Writer writer = null;
        try {
            /*
                outputStream = new FileOutputStream(rowFile);
                byte[] bytes = sb.toString().getBytes();
                outputStream.write(bytes);
                // writer.close();
             */
                Path path = Paths.get(rowFile.getAbsolutePath());
                StandardOpenOption writeOption = StandardOpenOption.TRUNCATE_EXISTING;
                if (!rowFile.exists())
                    writeOption = StandardOpenOption.CREATE;
                writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, writeOption);
                gson = new Gson();
                List<JSONWebAddress> jsonList = new ArrayList<>();
                for(WebAddresItem item : webAddressRows)
                {
                    jsonList.add(new JSONWebAddress(item));
                }
                gson.toJson(jsonList, writer);
        } catch (IOException ioe) {
                ioe.printStackTrace();
                labelMsg.setText("Write file " + rowFile.getAbsolutePath() + " error: " + ioe.getMessage());
        } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ioe2) {
                        ioe2.printStackTrace();
                        labelMsg.setText("Closing file " + rowFile.getAbsolutePath() + " error: " + ioe2.getMessage());
                    }
                }
        }
    }

    /*
    public static HttpsURLConnection getConnection(boolean ignoreInvalidCertificate, String user, String pass,
                                                   HttpRequestMethod httpRequestMethod,  URL url)
            throws KeyManagementException, NoSuchAlgorithmException, IOException{
        SSLContext ctx = SSLContext.getInstance("TLS");
        if (ignoreInvalidCertificate){
            ctx.init(null, new TrustManager[] { new InvalidCertificateTrustManager() }, null);
        }
        SSLContext.setDefault(ctx);

        String authStr = user+":"+pass;
        String authEncoded = Base64.encodeBytes(authStr.getBytes());

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + authEncoded);

        if (ignoreInvalidCertificate){
            connection.setHostnameVerifier(new InvalidCertificateHostVerifier());
        }

        return connection;
    }
    */

    private int getHttpsStatusCodeAfterGet(URL url)
          /*  throws NoSuchAlgorithmException */
    {
        int ret = -1;
        var trustManager = new X509ExtendedTrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException e0) {
            throw new RuntimeException(e0);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(url.toURI())
                    .header("Content-Type", "text/html")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient  client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            ret = response.statusCode();
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        } catch (IOException e3) {
            throw new RuntimeException(e3);
        }
        return ret;
    }

    boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private boolean isStringWebAddressOK(String strWebAddress)
    {
        boolean ret = false;
        if (strWebAddress != null && strWebAddress.trim().length()>0)
        {
            URL url = null;
           // try {
                /*
                url = new URL(strWebAddress);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                  //  responseCode = getHttpsStatusCodeAfterGet(url);
                   // if (responseCode != HttpURLConnection.HTTP_OK) {
                 */
                    if (!isValidURL(strWebAddress))
                        showWrongWebAddressDialog();
                    else
                        ret = true;
                   // }
                /*
                }
                else
                    ret = true;
                 */
            /*
            }
            catch (MalformedURLException e) {
                showWrongWebAddressDialog();
                return false;
          } catch (IOException ioe) {
                showWrongWebAddressDialog();
                return false;
          }
             */
        }
        return ret;
    }

    private void showWrongWebAddressDialog(){
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.WARNING,
                "The wrong web address in text field!" , okButtonType);
        alert.setTitle("Wrong web address");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-weight: bold");
        Optional<ButtonType> result = alert.showAndWait();
    }

    private boolean isWebAddressOk(WebAddresItem item)
    {
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        if (textFieldWebAddress.getText().trim().length() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Web address text field is empty, no charactres!\nAdd an address." , okButtonType);
            alert.setTitle("No saving row");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-font-weight: bold");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(cancelType) == okButtonType)
                return false;
        }

        String search = textFieldWebAddress.getText();
        String webaddress = null;
        int i = 0;
        for (WebAddresItem item2 : webAddressRows) {
            if (item2 == null) {
                i++;
                continue;
            }
            webaddress = item2.getWebaddress();
            if (webaddress != null && webaddress.equals(search)
                && item != item2 && item.getOrder() != item2.getOrder())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "The same web address is existing all ready!\nChange it before saving." , okButtonType);
                alert.setTitle("No saving row");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("-fx-font-weight: bold");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(cancelType) == okButtonType)
                    return false;
                return false;
            }
        }
        return true;
    }

    @FXML
    protected void pressedButtonSave()
    {
        // System.out.println("pressedButtonSave");
        int iSelectedInd = tableViewWebPages.getSelectionModel().getFocusedIndex();
        if (iSelectedInd > -1)
        {
            WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
            if (item != null)
            {
                if (!isWebAddressOk(item))
                    return;
                item.setStar(comboStar.getValue());
                item.setKeyword(textFieldKeyWord.getText());
                item.setTitle(textFieldTitle.getText());
                try {
                    new URL(textFieldWebAddress.getText());
                    item.setWebaddress(textFieldWebAddress.getText());
                    WebAddresItem searchItem = null;
                    int i = 0;
                    for (WebAddresItem item2 : webAddressRows)
                    {
                        if (item2 == null)
                        {
                            i++;
                            continue;
                        }
                        if (item2.getOrder() == item.getOrder()) {
                            searchItem = item2;
                            break;
                        }
                        i++;
                    }
                    if (searchItem != null)
                        webAddressRows.set(i, item);
                    saveWebAddressItems();
                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                    String msg = "A wrong web adress value: " +malformedURLException.getMessage();
                    System.out.println(msg);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            labelMsg.setText(msg);
                        }
                    });
                    return;
                }
            }
        }
        else
            labelMsg.setText("No selected table row! No saving.");
    }

    @FXML
    protected void pressedButtonOpenBrowser()
    {
     //   System.out.println("buttonOpenBrowser");
        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
        if (item != null)
        {
            String strUrl = item.getWebaddress();
            if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
            {
                System.out.println("not supported");
                return;
            }
            EventQueue.invokeLater(() -> {
                try {
                    Desktop.getDesktop().browse(new URL(strUrl).toURI());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    labelMsg.setText("Error: " +e1.getMessage());
                } catch (URISyntaxException e2) {
                    e2.printStackTrace();
                    labelMsg.setText("Error: " +e2.getMessage());
                } catch (Exception e3) {
                    e3.printStackTrace();
                    labelMsg.setText("Error: " +e3.getMessage());
                }
            });
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        // welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void pressedButtonListAll()
    {
        tableViewWebPages.setItems(webAddressRows);
        buttonListAll.setSelected(true);
    }

    @FXML
    protected void pressedButtonBookMark()
    {
     //   System.out.println("pressedButtonBookMark");
        if (bPressedButtonNewRow)
            return;
        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
        if (item != null)
        {
            boolean bModified = false;
            if (buttonBookMark.isSelected() && item.getBookmark() == 0) {
                item.setBookmark(1);
                bModified = true;
            }
            else
            if (!buttonBookMark.isSelected() && item.getBookmark() != 0) {
                item.setBookmark(0);
                bModified = true;
            }
            if (bModified)
            {
                WebAddresItem searchItem = null;
                int i = 0;
                for (WebAddresItem item2 : webAddressRows)
                {
                    if (item2 == null)
                    {
                        i++;
                        continue;
                    }
                    if (item2.getOrder() == item.getOrder()) {
                        searchItem = item2;
                        break;
                    }
                    i++;
                }
                if (searchItem != null) {
                    webAddressRows.set(i, item);
                    saveWebAddressItems();
                }
            }
        }
    }

    @FXML
    protected void pressedButtonNewRow()
    {
     //   System.out.println("pressedButtonNewRow");
        bPressedButtonNewRow = true;
        buttonNewRow.setDisable(true);
        buttonAdd.setDisable(false);
        buttonDelete.setDisable(true);
        textFieldShow.setText("");
        labelOrder.setText("");
        labelDate.setText(getTodayString());
        buttonBookMark.setSelected(false);
        comboStar.setValue("");
        textFieldKeyWord.setText("");
        textFieldWebAddress.setText("");
        textFieldTitle.setText("");

        buttonDelete.setDisable(true);
        buttonSave.setDisable(true);
        buttonOpenBrowser.setDisable(true);
    }

    @FXML
    protected void pressedButtonAdd()
    {
        String strWebAddress = textFieldWebAddress.getText();
        String title = textFieldTitle.getText();
        int iBookMark = (buttonBookMark.isSelected() ? 1 : 0);
        String strStar = comboStar.getValue();
        int iStars = (strStar == null || strStar.trim().length()==0 ? 0 : strStar.trim().length());
        String itemKWord = textFieldKeyWord.getText();
        int iOrder = webAddressRows.size() +1;
        if (iOrder > 1)
            iOrder = getMaxOrderValueFromRows();

        WebAddresItem newItem = new WebAddresItem(iOrder, iStars, getTodayString(),
                itemKWord, strWebAddress, title, iBookMark);;
        if (!isWebAddressOk(newItem))
            return;
        webAddressRows.add(newItem);
        saveWebAddressItems();
        bPressedButtonNewRow = false;

        buttonDelete.setDisable(false);
        buttonSave.setDisable(false);
        buttonOpenBrowser.setDisable(false);

        buttonAdd.setDisable(true);
        buttonNewRow.setDisable(false);
        selectWebAddressItemInTableView(newItem);
    }
}