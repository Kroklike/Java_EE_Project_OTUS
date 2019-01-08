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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;
import ru.otus.akn.project.gwt.client.model.NewsItemCreator;
import ru.otus.akn.project.gwt.client.model.PartnerItemCreator;
import ru.otus.akn.project.gwt.client.service.AuthorisationServiceAsync;
import ru.otus.akn.project.gwt.client.service.DepartmentServiceAsync;
import ru.otus.akn.project.gwt.client.service.EmployeeServiceAsync;
import ru.otus.akn.project.gwt.client.service.PositionServiceAsync;
import ru.otus.akn.project.gwt.shared.*;
import ru.otus.akn.project.gwt.shared.exception.WrongCredentialsException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.akn.project.gwt.client.Project.fixStat;
import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class CenterBlock extends Composite {

    public static final int MAIN_LINK_INDEX = 0;
    public static final String MAIN_PAGE_NAME = "MainPageGWT";
    public static final int ENTRANCE_LINK_INDEX = 1;
    public static final String ENTRANCE_PAGE_NAME = "EntrancePageGWT";
    public static final int EMPLOYEE_LIST_LINK_INDEX = 2;
    public static final String EMPLOYEE_PAGE_NAME = "EmployeePageGWT";
    public static final int MATERIAL_LINK_INDEX = 3;
    public static final String MATERIAL_PAGE_NAME = "MaterialsPageGWT";
    public static final int PRICES_LINK_INDEX = 4;
    public static final String PRICES_PAGE_NAME = "PricesPageGWT";
    public static final int PROJECTS_LINK_INDEX = 5;
    public static final String PROJECTS_PAGE_NAME = "ProjectsPageGWT";
    private static final String CHECK_ONLY_LETTERS = "[^a-zA-Zа-яА-Я]*";
    private static final String CHECK_ONLY_LETTERS_AND_SPACE = "[^a-zA-Zа-яА-Я ]*";
    private static final String ONLY_NUMBERS_CHECK = "[^0-9]*";

    @UiTemplate("CenterBlock.ui.xml")
    public interface CenterBlockUiBinder extends UiBinder<DeckPanel, CenterBlock> {
    }

    private static final Logger LOGGER = Logger.getLogger(CenterBlock.class.getSimpleName());
    private static final ApplicationConstants CONSTANTS = INSTANCE.getConstants();
    private static CenterBlockUiBinder centerBlockUiBinder = INSTANCE.getCenterBlockUiBinder();
    private AuthorisationServiceAsync authorisationService = INSTANCE.getAuthorisationService();
    private EmployeeServiceAsync employeeService = INSTANCE.getEmployeeService();
    private DepartmentServiceAsync departmentService = INSTANCE.getDepartmentService();
    private PositionServiceAsync positionService = INSTANCE.getPositionService();
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
    FlowPanel employeePanel;

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

        fixStat(MAIN_PAGE_NAME);
        mainBlock.showWidget(MAIN_LINK_INDEX);
    }

    private void initTableBlock() {

        DecoratorPanel searchPanel = new DecoratorPanel();
        searchPanel.add(formSearchArea());

        DataGrid<Employee> table = new DataGrid<>();
        table.setSize("100%", "100%");
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        initFirstNameColumn(table);
        initLastNameColumn(table);
        initMiddleNameColumn(table);
        DynamicSelectionCell departmentCell = initDepartmentColumn(table);
        DynamicSelectionCell positionCell = initPositionColumn(table);
        initSalaryColumn(table);
        initDeleteButton(table);
        initAddNewButton(table);

        table.setTitle(CONSTANTS.centerBlockLoginAfter());
        employeeDataGrid = table;

        departmentService.getAllDepartments(new AsyncCallback<List<Department>>() {
            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(List<Department> result) {
                List<String> departmentNames = new ArrayList<>();
                for (Department department : result) {
                    departmentNames.add(department.getDepartmentName());
                }
                departmentCell.addOptions(departmentNames);
            }
        });

        positionService.getAllPositions(new AsyncCallback<List<Position>>() {
            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(List<Position> result) {
                List<String> positionNames = new ArrayList<>();
                for (Position position : result) {
                    positionNames.add(position.getPositionName());
                }
                positionCell.addOptions(positionNames);
            }
        });

        employeePanel.add(searchPanel);
        employeePanel.add(table);
    }

    private FlexTable formSearchArea() {
        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);

        FlexTable.FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        layout.setHTML(0, 0, CONSTANTS.employeeTableSearchForm());
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(
                0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        layout.setHTML(1, 0, CONSTANTS.employeeTableFirstName() + ":");
        TextArea firstNameArea = new TextArea();
        layout.setWidget(1, 1, firstNameArea);
        layout.setHTML(1, 2, CONSTANTS.employeeTableLastName() + ":");
        TextArea lastNameArea = new TextArea();
        layout.setWidget(1, 3, lastNameArea);
        layout.setHTML(1, 4, CONSTANTS.employeeTableMiddleName() + ":");
        TextArea middleNameArea = new TextArea();
        layout.setWidget(1, 5, middleNameArea);
        layout.setHTML(2, 0, CONSTANTS.employeeTablePosition() + ":");
        TextArea positionArea = new TextArea();
        layout.setWidget(2, 1, positionArea);
        layout.setHTML(2, 2, CONSTANTS.employeeTableTown() + ":");
        TextArea townArea = new TextArea();
        layout.setWidget(2, 3, townArea);
        layout.setHTML(3, 0, CONSTANTS.employeeTableAgeFrom() + ":");
        TextArea ageAreaFrom = new TextArea();
        layout.setWidget(3, 1, ageAreaFrom);
        layout.setHTML(3, 2, CONSTANTS.employeeTableAgeTo() + ":");
        TextArea ageAreaTo = new TextArea();
        layout.setWidget(3, 3, ageAreaTo);
        Button findButton = new Button(CONSTANTS.employeeTableFind());
        layout.setWidget(4, 0, findButton);

        findButton.addClickHandler(event -> {
            StringBuilder errorsList = new StringBuilder();
            if (!firstNameArea.getValue().isEmpty() &&
                    firstNameArea.getValue().matches(CHECK_ONLY_LETTERS)) {
                errorsList.append(CONSTANTS.employeeTableFirstNameOnlyLetters() + "\n");
            }
            if (!lastNameArea.getValue().isEmpty() &&
                    lastNameArea.getValue().matches(CHECK_ONLY_LETTERS)) {
                errorsList.append(CONSTANTS.employeeTableLastNameOnlyLetters() + "\n");
            }
            if (!middleNameArea.getValue().isEmpty() &&
                    middleNameArea.getValue().matches(CHECK_ONLY_LETTERS)) {
                errorsList.append(CONSTANTS.employeeTableMiddleNameOnlyLetters() + "\n");
            }
            if (!positionArea.getValue().isEmpty() &&
                    positionArea.getValue().matches(CHECK_ONLY_LETTERS_AND_SPACE)) {
                errorsList.append(CONSTANTS.employeeTablePositionOnlyLetters() + "\n");
            }
            if (!townArea.getValue().isEmpty() &&
                    townArea.getValue().matches(CHECK_ONLY_LETTERS_AND_SPACE)) {
                errorsList.append(CONSTANTS.employeeTableTownOnlyLetters() + "\n");
            }
            if (!ageAreaFrom.getValue().isEmpty() &&
                    ageAreaFrom.getValue().matches(ONLY_NUMBERS_CHECK)) {
                errorsList.append(CONSTANTS.employeeTableAgeFromOnlyNumbers() + "\n");
            }
            if (!ageAreaTo.getValue().isEmpty() &&
                    ageAreaTo.getValue().matches(ONLY_NUMBERS_CHECK)) {
                errorsList.append(CONSTANTS.employeeTableAgeToOnlyNumbers() + "\n");
            }
            if (!ageAreaFrom.getValue().isEmpty() && !ageAreaTo.getValue().isEmpty() &&
                    !ageAreaFrom.getValue().matches(ONLY_NUMBERS_CHECK) && !ageAreaTo.getValue().matches(ONLY_NUMBERS_CHECK)) {
                BigDecimal ageFrom = new BigDecimal(ageAreaFrom.getValue());
                BigDecimal ageTo = new BigDecimal(ageAreaTo.getValue());
                if (ageFrom.compareTo(ageTo) > 0) {
                    errorsList.append(CONSTANTS.employeeTableAgeFromToCompare());
                }
            }
            if (!errorsList.toString().isEmpty()) {
                Window.alert(errorsList.toString());
            } else {
                Filter filter = new Filter();
                filter.setFirstName(firstNameArea.getValue());
                filter.setLastName(lastNameArea.getValue());
                filter.setMiddleName(middleNameArea.getValue());
                filter.setPosition(positionArea.getValue());
                filter.setTown(townArea.getValue());
                filter.setAgeFrom(ageAreaFrom.getValue());
                filter.setAgeTo(ageAreaTo.getValue());
                employeeService.findEmployee(filter, new AsyncCallback<List<Employee>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(List<Employee> result) {
                        Window.open("http://localhost:8080/resultPage.jsp", "", "");
                    }
                });
            }
        });

        return layout;
    }

    private void initAddNewButton(DataGrid<Employee> table) {
        Column<Employee, String> addBtn = new Column<Employee, String>(
                new ButtonCell()) {
            @Override
            public String getValue(Employee c) {
                return "+";
            }
        };

        table.addColumn(addBtn, "");

        addBtn.setFieldUpdater((index, employee, value) ->
                updateDataGridWithNewRow());
    }

    private void initDeleteButton(DataGrid<Employee> table) {
        Column<Employee, String> deleteBtn = new Column<Employee, String>(
                new ButtonCell()) {
            @Override
            public String getValue(Employee c) {
                return "x";
            }
        };

        table.addColumn(deleteBtn, "");

        deleteBtn.setFieldUpdater((index, employee, value) ->
        {
            if (employee.getId() != null) {
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
                        });
            } else {
                updateDataGrid();
            }
        });
    }

    private void initSalaryColumn(DataGrid<Employee> table) {
        TextInputCell salaryCell = new TextInputCell();
        Column<Employee, String> salary = new Column<Employee, String>(salaryCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getSalary().toString();
            }
        };
        table.addColumn(salary, CONSTANTS.employeeTableSalary());
        salary.setFieldUpdater((index, row, value) -> {
            if (value.matches(ONLY_NUMBERS_CHECK)) {
                Window.alert(CONSTANTS.employeeTableSalaryOnlyNumbers());
                return;
            } else if (!row.isReadyToSave()) {
                row.setSalary(new BigDecimal(value));
                return;
            }
            row.setSalary(new BigDecimal(value));
            updateEmployee(row);
        });
    }

    private DynamicSelectionCell initPositionColumn(DataGrid<Employee> table) {
        DynamicSelectionCell positionCell = new DynamicSelectionCell();

        Column<Employee, String> position = new Column<Employee, String>(positionCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getPositionName();
            }
        };
        table.addColumn(position, CONSTANTS.employeeTablePosition());

        position.setFieldUpdater((index, employee, value) -> {
            if (!employee.isReadyToSave()) {
                employee.setPositionName(value);
                return;
            }
            employee.setPositionName(value);
            updateEmployee(employee);
        });
        return positionCell;
    }

    private DynamicSelectionCell initDepartmentColumn(DataGrid<Employee> table) {
        DynamicSelectionCell departmentCell = new DynamicSelectionCell();
        Column<Employee, String> department = new Column<Employee, String>(departmentCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getDepartmentName();
            }
        };
        table.addColumn(department, CONSTANTS.employeeTableDepartment());

        department.setFieldUpdater((index, employee, value) -> {
            if (!employee.isReadyToSave()) {
                employee.setDepartmentName(value);
                return;
            }
            employee.setDepartmentName(value);
            updateEmployee(employee);
        });
        return departmentCell;
    }

    private void initMiddleNameColumn(DataGrid<Employee> table) {
        TextInputCell middleNameCell = new TextInputCell();
        Column<Employee, String> middleName = new Column<Employee, String>(middleNameCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getMiddleName();
            }
        };
        table.addColumn(middleName, CONSTANTS.employeeTableMiddleName());
        middleName.setFieldUpdater((index, row, value) -> {
            if (value != null && !value.isEmpty() && value.matches(CHECK_ONLY_LETTERS)) {
                Window.alert(CONSTANTS.employeeTableMiddleNameOnlyLetters());
                return;
            } else if (!row.isReadyToSave()) {
                row.setMiddleName(value);
                return;
            }
            row.setMiddleName(value);
            updateEmployee(row);
        });
    }

    private void initLastNameColumn(DataGrid<Employee> table) {
        TextInputCell lastNameCell = new TextInputCell();
        Column<Employee, String> lastName = new Column<Employee, String>(lastNameCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getLastName();
            }
        };
        table.addColumn(lastName, CONSTANTS.employeeTableLastName());
        lastName.setFieldUpdater((index, row, value) -> {
            if (value == null || value.isEmpty()) {
                Window.alert(CONSTANTS.employeeTableLastNameNotNull());
                return;
            } else if (value.matches(CHECK_ONLY_LETTERS)) {
                Window.alert(CONSTANTS.employeeTableLastNameOnlyLetters());
                return;
            } else if (!row.isReadyToSave()) {
                row.setLastName(value);
                return;
            }
            row.setLastName(value);
            updateEmployee(row);
        });
    }

    private void initFirstNameColumn(DataGrid<Employee> table) {
        TextInputCell firstNameCell = new TextInputCell();
        Column<Employee, String> firstName = new Column<Employee, String>(firstNameCell) {
            @Override
            public String getValue(Employee employee) {
                return employee.getFirstName();
            }
        };

        table.addColumn(firstName, CONSTANTS.employeeTableFirstName());

        firstName.setFieldUpdater((index, row, value) -> {
            if (value == null || value.isEmpty()) {
                Window.alert(CONSTANTS.employeeTableFirstNameNotNull());
                return;
            } else if (value.matches(CHECK_ONLY_LETTERS)) {
                Window.alert(CONSTANTS.employeeTableFirstNameOnlyLetters());
                return;
            } else if (!row.isReadyToSave()) {
                row.setFirstName(value);
                return;
            }
            row.setFirstName(value);
            updateEmployee(row);
        });
    }

    private void updateEmployee(Employee employee) {
        if (employee.getId() == null) {
            employeeService.addNewEmployee(employee, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    updateDataGrid();
                }
            });
        } else {
            employeeService.updateEmployee(employee, new AsyncCallback<Void>() {
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
    }

    private void initNewsBlock() {

        Style innerStyle = newsBlock.getElement().getStyle();
        innerStyle.clearHeight();

        Style innerStyleParent = newsBlock.getElement().getParentElement().getStyle();
        innerStyleParent.clearHeight();

        FlowPanel materialNewsDescription = new FlowPanel();
        materialNewsDescription.add(new Label(CONSTANTS.centerBlockNewsMaterialsFirstMessage()));
        Anchor materialsLink = new Anchor(CONSTANTS.centerBlockNewsMaterialsAnchorText());
        materialsLink.addClickHandler(event -> {
            fixStat(MATERIAL_PAGE_NAME);
            mainBlock.showWidget(MATERIAL_LINK_INDEX);
        });
        materialNewsDescription.add(materialsLink);
        materialNewsDescription.add(new Label(CONSTANTS.centerBlockNewsMaterialsSecondMessage()));

        NewsItemCreator materialNewsItem = new NewsItemCreator("../images/materials.png",
                CONSTANTS.centerBlockNewsMaterialsHeader(), materialNewsDescription,
                new Date(getYearForDateType(2018), getMonthForDateType(9), 14));

        FlowPanel pricesDescription = new FlowPanel();
        pricesDescription.add(new Label(CONSTANTS.centerBlockNewsPricesMessage()));
        Anchor pricesLink = new Anchor(CONSTANTS.centerBlockNewsPricesAnchorText());
        pricesLink.addClickHandler(event -> {
            fixStat(PRICES_PAGE_NAME);
            mainBlock.showWidget(PRICES_LINK_INDEX);
        });
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
                Window.alert(CONSTANTS.loginSuccess());
                fixStat(EMPLOYEE_PAGE_NAME);
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
                employeeDataGrid.redraw();
            }
        });
    }

    private void updateDataGridWithNewRow() {
        employeeService.getAllEmployees(new AsyncCallback<List<Employee>>() {
            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(List<Employee> result) {
                result.add(createEmptyEmployee());
                employeeDataGrid.setRowCount(result.size(), true);
                employeeDataGrid.setRowData(0, result);
                employeeDataGrid.redraw();
            }
        });
    }

    private Employee createEmptyEmployee() {
        Employee newRow = new Employee();
        newRow.setSalary(new BigDecimal(0));
        newRow.setFirstName("");
        newRow.setLastName("");
        return newRow;
    }

    private int getYearForDateType(int normalYear) {
        return normalYear - 1900;
    }

    private int getMonthForDateType(int normalMonth) {
        return normalMonth - 1;
    }
}