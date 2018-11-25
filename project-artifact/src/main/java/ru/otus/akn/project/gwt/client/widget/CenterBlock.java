package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;
import ru.otus.akn.project.gwt.client.model.NewsItemCreator;
import ru.otus.akn.project.gwt.client.model.PartnerItemCreator;
import ru.otus.akn.project.gwt.client.service.AuthorisationServiceAsync;
import ru.otus.akn.project.gwt.client.service.EmployeeServiceAsync;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.gwt.shared.User;
import ru.otus.akn.project.gwt.shared.exception.WrongCredentialsException;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class CenterBlock extends Composite {

    public static final int MAIN_LINK_INDEX = 0;
    public static final int ENTRANCE_LINK_INDEX = 1;
    public static final int EMPLOYEE_LIST_LINK_INDEX = 2;
    public static final int MATERIAL_LINK_INDEX = 3;
    public static final int PRICES_LINK_INDEX = 4;
    public static final int PROJECTS_LINK_INDEX = 5;

    @UiTemplate("CenterBlock.ui.xml")
    public interface CenterBlockUiBinder extends UiBinder<DeckPanel, CenterBlock> {
    }

    private static final Logger LOGGER = Logger.getLogger(CenterBlock.class.getSimpleName());
    private static final ApplicationConstants CONSTANTS = INSTANCE.getConstants();
    private static CenterBlockUiBinder centerBlockUiBinder = INSTANCE.getCenterBlockUiBinder();
    private AuthorisationServiceAsync authorisationService = INSTANCE.getAuthorisationService();
    private EmployeeServiceAsync employeeService = INSTANCE.getEmployeeService();
    private DataGrid<Employee> employeeDataGrid;

    @UiField
    DeckPanel mainBlock;
    @UiField
    FlowPanel materialBlock;
    @UiField
    FlowPanel newsBlock;
    @UiField
    TextBox loginTextField;
    @UiField
    TextBox passwordTextField;
    @UiField
    SimpleLayoutPanel employeePanel;

    @Inject
    public CenterBlock() {
        initWidget(centerBlockUiBinder.createAndBindUi(this));

        for (int widgetIndex = 0; widgetIndex < mainBlock.getWidgetCount(); widgetIndex++) {
            Element parent = DOM.getParent(mainBlock.getWidget(widgetIndex).getElement());
            parent.getStyle().clearHeight();
        }

        initNewsBlock();
        initMaterialBlock();
        initTableBlock();

        mainBlock.showWidget(MAIN_LINK_INDEX);
    }

    private void initTableBlock() {

        DataGrid<Employee> table = new DataGrid<>();
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        TextInputCell fullNameCell = new TextInputCell();
        Column<Employee, String> fullName = new Column<Employee, String>(fullNameCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getFullName();
            }
        };
        table.addColumn(fullName, "Full name");
        fullName.setFieldUpdater((index, row, value) -> {
            String[] strings = value.split(" ");
            if (strings.length > 3 || strings.length < 2) {
                throw new RuntimeException("Получено некорректное ФИО");
            } else {
                for (String partOfName : strings) {
                    if (partOfName.contains("[^a-za-Zа-яА-Я]")) {
                        throw new RuntimeException("ФИО должно содержать только буквы и пробелы");
                    }
                }
                row.setFullName(value);
                employeeService.updateEmployee(row, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        updateDataGrid();
                    }
                });
            }
        });

        TextColumn<Employee> department = new TextColumn<Employee>() {
            @Override
            public String getValue(Employee employee) {
                return employee.getDepartmentName();
            }
        };
        table.addColumn(department, "Department");

        TextColumn<Employee> position = new TextColumn<Employee>() {
            @Override
            public String getValue(Employee employee) {
                return employee.getPositionName();
            }
        };
        table.addColumn(position, "Position");

        TextColumn<Employee> salary = new TextColumn<Employee>() {
            @Override
            public String getValue(Employee employee) {
                return employee.getSalary().toString();
            }
        };
        table.addColumn(salary, "Salary");

        Column<Employee, String> deleteBtn = new Column<Employee, String>(
                new ButtonCell()) {
            @Override
            public String getValue(Employee c) {
                return "x";
            }
        };

        table.addColumn(deleteBtn, "");

        deleteBtn.setFieldUpdater((index, employee, value) ->
                employeeService.deleteEmployeeById(employee.getId(),
                        new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
                            }

                            @Override
                            public void onSuccess(Void result) {
                                updateDataGrid();
                            }
                        }));

        table.setTitle(CONSTANTS.centerBlockLoginAfter());
        employeeDataGrid = table;

        employeePanel.add(table);
    }

    private void initNewsBlock() {

        Style innerStyle = newsBlock.getElement().getStyle();
        innerStyle.clearHeight();

        Style innerStyleParent = newsBlock.getElement().getParentElement().getStyle();
        innerStyleParent.clearHeight();

        FlowPanel materialNewsDescription = new FlowPanel();
        materialNewsDescription.add(new Label(CONSTANTS.centerBlockNewsMaterialsFirstMessage()));
        Anchor materialsLink = new Anchor(CONSTANTS.centerBlockNewsMaterialsAnchorText());
        materialsLink.addClickHandler(event -> mainBlock.showWidget(MATERIAL_LINK_INDEX));
        materialNewsDescription.add(materialsLink);
        materialNewsDescription.add(new Label(CONSTANTS.centerBlockNewsMaterialsSecondMessage()));

        NewsItemCreator materialNewsItem = new NewsItemCreator("../images/materials.png",
                CONSTANTS.centerBlockNewsMaterialsHeader(), materialNewsDescription,
                new Date(getYearForDateType(2018), getMonthForDateType(9), 14));

        FlowPanel pricesDescription = new FlowPanel();
        pricesDescription.add(new Label(CONSTANTS.centerBlockNewsPricesMessage()));
        Anchor pricesLink = new Anchor(CONSTANTS.centerBlockNewsPricesAnchorText());
        pricesLink.addClickHandler(event -> mainBlock.showWidget(PRICES_LINK_INDEX));
        pricesDescription.add(pricesLink);

        NewsItemCreator pricesNewsItem = new NewsItemCreator("../images/prices.jpg",
                CONSTANTS.centerBlockNewsPricesHeader(), pricesDescription,
                new Date(getYearForDateType(2018), getMonthForDateType(7), 10));

        FlowPanel firstProjectDescription = new FlowPanel();
        firstProjectDescription.add(new Label(CONSTANTS.centerBlockNewsProjectMessage()));

        NewsItemCreator firstProjectNewsItem = new NewsItemCreator("../images/first_project.jpg",
                CONSTANTS.centerBlockNewsProjectHeader(), firstProjectDescription,
                new Date(getYearForDateType(2018), getMonthForDateType(5), 14));

        FlowPanel openingProjectDescription = new FlowPanel();
        openingProjectDescription.add(new Label(CONSTANTS.centerBlockNewsOpeningMessage()));

        NewsItemCreator openingNewsItem = new NewsItemCreator("../images/opening.jpg",
                CONSTANTS.centerBlockNewsOpeningHeader(), openingProjectDescription,
                new Date(getYearForDateType(2018), getMonthForDateType(4), 12));

        newsBlock.add(materialNewsItem.getNewsItem());
        newsBlock.add(pricesNewsItem.getNewsItem());
        newsBlock.add(firstProjectNewsItem.getNewsItem());
        newsBlock.add(openingNewsItem.getNewsItem());
    }

    private void initMaterialBlock() {

        materialBlock.addStyleName("center-block");
        Style innerStyle = materialBlock.getElement().getStyle();
        innerStyle.clearHeight();

        PartnerItemCreator savewood = new PartnerItemCreator("../images/savewood.png",
                CONSTANTS.centerBlockMaterialsHeaderSaveWood(),
                CONSTANTS.centerBlockMaterialsDescriptionSaveWood(),
                "https://www.savewood.ru/",
                CONSTANTS.centerBlockMaterialsKnowMore());

        PartnerItemCreator favorit = new PartnerItemCreator("../images/favorit.jpg",
                CONSTANTS.centerBlockMaterialsHeaderFavorit(),
                CONSTANTS.centerBlockMaterialsDescriptionFavorit(),
                "http://www.deluxe-ccc.ru/",
                CONSTANTS.centerBlockMaterialsKnowMore());

        PartnerItemCreator ruchidel = new PartnerItemCreator("../images/ruch.jpg",
                CONSTANTS.centerBlockMaterialsHeaderRuchidel(),
                CONSTANTS.centerBlockMaterialsDescriptionRuchidel(),
                "http://www.shirmel.ru/",
                CONSTANTS.centerBlockMaterialsKnowMore());

        PartnerItemCreator sistrom = new PartnerItemCreator("../images/sistrom.png",
                CONSTANTS.centerBlockMaterialsHeaderSistrom(),
                CONSTANTS.centerBlockMaterialsDescriptionSistrom(),
                "http://www.sistrom.ru/",
                CONSTANTS.centerBlockMaterialsKnowMore());

        materialBlock.add(savewood.getItem());
        materialBlock.add(favorit.getItem());
        materialBlock.add(ruchidel.getItem());
        materialBlock.add(sistrom.getItem());
    }

    @UiHandler("submit")
    void clickHandler(ClickEvent evt) {
        User user = new User(loginTextField.getValue(), passwordTextField.getValue());
        authorisationService.authorize(user, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof WrongCredentialsException) {
                    Window.alert(caught.getLocalizedMessage());
                }
            }

            @Override
            public void onSuccess(Void result) {
                Window.alert("Вход успешен!");
                mainBlock.showWidget(EMPLOYEE_LIST_LINK_INDEX);

                updateDataGrid();
            }
        });
    }

    private void updateDataGrid() {
        employeeService.getAllEmployees(new AsyncCallback<List<Employee>>() {
            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(List<Employee> result) {
                employeeDataGrid.setRowCount(result.size(), true);
                employeeDataGrid.setRowData(0, result);
            }
        });
    }

    private int getYearForDateType(int normalYear) {
        return normalYear - 1900;
    }

    private int getMonthForDateType(int normalMonth) {
        return normalMonth - 1;
    }
}