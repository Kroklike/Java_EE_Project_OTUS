CREATE SEQUENCE EMPLOYEES_SEQ;

CREATE TABLE EMPLOYEES
  (EMPLOYEE_ID NUMBER (19, 0) NOT NULL,
  POSITION_ID NUMBER (19, 0) NOT NULL,
  DEPARTMENT_ID NUMBER (19, 0) NOT NULL,
  FIRST_NAME VARCHAR2 (255 CHAR) NOT NULL,
  LAST_NAME VARCHAR2 (255 CHAR) NOT NULL,
  EMPLOYMENT_DATE DATE NOT NULL,
  SALARY_INFO_ID NUMBER (19, 0) NOT NULL,
  MIDDLE_NAME VARCHAR2 (255 CHAR),
  TELEPHONE_NUMBER VARCHAR2 (255 CHAR),
  EMAIL VARCHAR2 (255 CHAR),
  DISMISSAL_DATE DATE,
  USER_ID NUMBER (19, 0),
  CONSTRAINT PK_EMPLOYEE_ID PRIMARY KEY (EMPLOYEE_ID));

CREATE SEQUENCE POSITIONS_SEQ;

CREATE TABLE POSITIONS
  (POSITION_ID NUMBER (19, 0) NOT NULL,
    POSITION_NAME VARCHAR2 (255 CHAR) NOT NULL,
    CONSTRAINT PK_POSITION_ID PRIMARY KEY (POSITION_ID));

CREATE SEQUENCE DEPARTMENTS_SEQ;

CREATE TABLE DEPARTMENTS
  (DEPARTMENT_ID NUMBER (19, 0) NOT NULL,
  DEPARTMENT_NAME VARCHAR2 (255 CHAR) NOT NULL,
  CITY VARCHAR2 (255 CHAR),
  FULL_ADDRESS VARCHAR2 (1000 CHAR),
  CONSTRAINT PK_DEPARTMENT_ID PRIMARY KEY (DEPARTMENT_ID));

CREATE SEQUENCE PERMISSIONS_SEQ;

CREATE TABLE PERMISSIONS
  (PERMISSION_ID NUMBER (19, 0) NOT NULL,
  PERMISSION_NAME VARCHAR2 (255 CHAR) NOT NULL,
  PERMISSION_CODE VARCHAR2 (255 CHAR) NOT NULL,
  PERMISSION_DESCRIPTION VARCHAR2 (1000 CHAR) NOT NULL,
  CONSTRAINT PK_PERMISSION_ID PRIMARY KEY (PERMISSION_ID));

CREATE SEQUENCE SALARY_INFO_SEQ;

CREATE TABLE SALARY_INFO
  (SALARY_INFO_ID NUMBER (19, 0) NOT NULL,
  SALARY NUMBER (19, 2) NOT NULL,
  BONUS_PERCENT NUMBER (12, 4) DEFAULT 0 NOT NULL,
  CONSTRAINT PK_SALARY_INFO_ID PRIMARY KEY (SALARY_INFO_ID));

CREATE SEQUENCE USERS_SEQ;

CREATE TABLE USERS
  (USER_ID NUMBER (19, 0) NOT NULL,
  PASSWORD_HASH VARCHAR2 (255 CHAR) NOT NULL,
  LOGIN VARCHAR2 (255 CHAR) NOT NULL,
  IS_ACTIVE NUMBER (1, 0) DEFAULT 0 NOT NULL,
  CONSTRAINT PK_USER_ID PRIMARY KEY (USER_ID));

CREATE SEQUENCE AUDIT_HISTORIES_SEQ;

CREATE TABLE AUDIT_HISTORIES
  (AUDIT_HISTORY_ID NUMBER (19, 0) NOT NULL,
  EXECUTION_DATE DATE NOT NULL,
  AUDIT_OPERATION_ID NUMBER (19, 0) NOT NULL,
  USER_ID NUMBER (19, 0) NOT NULL,
  IS_ERROR NUMBER (1, 0) NOT NULL,
  ERROR_INFO VARCHAR2 (1000 CHAR),
  CONSTRAINT PK_AUDIT_HISTORY_ID PRIMARY KEY (AUDIT_HISTORY_ID));

CREATE SEQUENCE AUDIT_OPERATIONS_SEQ;

CREATE TABLE AUDIT_OPERATIONS
  (AUDIT_OPERATION_ID NUMBER (19, 0) NOT NULL,
  OPERATION_NAME VARCHAR2 (255 CHAR) NOT NULL,
  OPERATION_CODE VARCHAR2 (255 CHAR) NOT NULL,
  OPERATION_DESCRIPTION VARCHAR2 (1000 CHAR) NOT NULL,
  CONSTRAINT PK_AUDIT_OPERATION_ID PRIMARY KEY (AUDIT_OPERATION_ID));

CREATE TABLE POSITION_PERMISSION_XREF
  (POSITION_ID NUMBER (19, 0) NOT NULL,
   PERMISSION_ID NUMBER (19, 0) NOT NULL,
   CONSTRAINT PK_POSITION_PERMISSION PRIMARY KEY (POSITION_ID, PERMISSION_ID));

--FOREIGN KEYS

ALTER TABLE EMPLOYEES
ADD CONSTRAINT EMPLOYEE_POSITION_FK
   FOREIGN KEY (POSITION_ID)
   REFERENCES POSITIONS (POSITION_ID);

ALTER TABLE EMPLOYEES
ADD CONSTRAINT EMPLOYEE_DEPARTMENT_FK
   FOREIGN KEY (DEPARTMENT_ID)
   REFERENCES DEPARTMENTS (DEPARTMENT_ID);

ALTER TABLE EMPLOYEES
ADD CONSTRAINT EMPLOYEE_SALARY_INFO_FK
   FOREIGN KEY (SALARY_INFO_ID)
   REFERENCES SALARY_INFO (SALARY_INFO_ID);

ALTER TABLE EMPLOYEES
ADD CONSTRAINT EMPLOYEE_USER_FK
   FOREIGN KEY (USER_ID)
   REFERENCES USERS (USER_ID);

ALTER TABLE EMPLOYEES
ADD CONSTRAINT UQ_EMPLOYEE_USER UNIQUE (EMPLOYEE_ID, USER_ID);

ALTER TABLE AUDIT_HISTORIES
ADD CONSTRAINT AUDIT_HISTORY_USER_FK
   FOREIGN KEY (USER_ID)
   REFERENCES USERS (USER_ID);

ALTER TABLE AUDIT_HISTORIES
ADD CONSTRAINT AUDIT_HISTORY_OPERATION_FK
   FOREIGN KEY (AUDIT_OPERATION_ID)
   REFERENCES AUDIT_OPERATIONS (AUDIT_OPERATION_ID);

ALTER TABLE AUDIT_HISTORIES
ADD CONSTRAINT IS_ERROR_BOOLEAN_CHECK
   CHECK (IS_ERROR IN (0, 1));

ALTER TABLE USERS
ADD CONSTRAINT IS_ACTIVE_BOOLEAN_CHECK
   CHECK (IS_ACTIVE IN (0, 1));

ALTER TABLE USERS
ADD CONSTRAINT UQ_USER_LOGIN UNIQUE (LOGIN);

--INDEXES

CREATE INDEX EMPLOYEE_POSITION_IDX ON EMPLOYEES (POSITION_ID);
CREATE INDEX EMPLOYEE_DEPARTMENT_IDX ON EMPLOYEES (DEPARTMENT_ID);
CREATE INDEX EMPLOYEE_USER_IDX ON EMPLOYEES (USER_ID);
CREATE INDEX EMPLOYEE_SALARY_INFO_IDX ON EMPLOYEES (SALARY_INFO_ID);
CREATE INDEX AUDIT_HISTORY_OPERATION_IDX ON AUDIT_HISTORIES (AUDIT_OPERATION_ID);
CREATE INDEX AUDIT_HISTORY_USER_IDX ON AUDIT_HISTORIES (USER_ID);

--TRIGGERS

CREATE OR REPLACE TRIGGER TRG_EMPLOYEE_ID
  BEFORE INSERT ON EMPLOYEES
  FOR EACH ROW
BEGIN
  :new.EMPLOYEE_ID := EMPLOYEES_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_SALARY_INFO_ID
  BEFORE INSERT ON SALARY_INFO
  FOR EACH ROW
BEGIN
  :new.SALARY_INFO_ID := SALARY_INFO_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_POSITION_ID
  BEFORE INSERT ON POSITIONS
  FOR EACH ROW
BEGIN
  :new.POSITION_ID := POSITIONS_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_DEPARTMENT_ID
  BEFORE INSERT ON DEPARTMENTS
  FOR EACH ROW
BEGIN
  :new.DEPARTMENT_ID := DEPARTMENTS_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_PERMISSION_ID
  BEFORE INSERT ON PERMISSIONS
  FOR EACH ROW
BEGIN
  :new.PERMISSION_ID := PERMISSIONS_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_USER_ID
  BEFORE INSERT ON USERS
  FOR EACH ROW
BEGIN
  :new.USER_ID := USERS_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_AUDIT_HISTORY_ID
  BEFORE INSERT ON AUDIT_HISTORIES
  FOR EACH ROW
BEGIN
  :new.AUDIT_HISTORY_ID := AUDIT_HISTORIES_SEQ.nextval;
END;

CREATE OR REPLACE TRIGGER TRG_AUDIT_OPERATION_ID
  BEFORE INSERT ON AUDIT_OPERATIONS
  FOR EACH ROW
BEGIN
  :new.AUDIT_OPERATION_ID := AUDIT_OPERATIONS_SEQ.nextval;
END;
