create or replace procedure employee_with_max_salary(
  cv_results out sys_refcursor)
is
  begin
    open cv_results for
    select * from EMPLOYEES E WHERE E.SALARY = (SELECT MAX(salary) FROM EMPLOYEES);
  end;
/

create or replace procedure get_statistic_road_map(
  p_stat_mark   in  varchar2,
  stat_road_map out sys_refcursor)
is
  begin
    open stat_road_map for
    select IP_ADDRESS, MAX(STATISTIC_ID) from STATISTICS where STATISTIC_MARK = p_stat_mark group by IP_ADDRESS;
  end;
/

create or replace procedure save_statistic_info
  (p_stat_mark    in  varchar2,
   p_page_name    in  varchar2,
   p_ip_address   in  varchar2,
   p_browser_info in  varchar2,
   p_visit_date   in  date,
   p_cookies      in  varchar2,
   p_req_params   in  varchar2,
   p_prev_stat_id in  number,
   ref_result     out sys_refcursor)

is
  p_curr_id number := STATISTICS_SEQ.nextval;
  begin

    insert into STATISTICS (STATISTIC_ID,
                            STATISTIC_MARK,
                            PAGE_NAME,
                            IP_ADDRESS,
                            BROWSER_INFO,
                            VISIT_DATE,
                            COOKIES,
                            REQUESTED_PARAMS,
                            PREV_STATISTIC_ID)
    values (p_curr_id,
            p_stat_mark,
            p_page_name,
            p_ip_address,
            p_browser_info,
            p_visit_date,
            p_cookies,
            p_req_params,
            p_prev_stat_id);

    commit;

    open ref_result for select p_curr_id from dual;

    exception
    when others
    then
      rollback;
      raise;
  end;
/