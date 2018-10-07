create or replace procedure employee_with_max_salary(
   cv_results out sys_refcursor)
   is
   begin
     open cv_results for
       select * from EMPLOYEES E WHERE E.SALARY = (SELECT MAX(salary) FROM EMPLOYEES);
end;