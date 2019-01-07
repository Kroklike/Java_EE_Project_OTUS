package ru.otus.akn.project.db.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "STATISTICS")
@ToString
public class StatisticEntity {

    @Id
    @Column(name = "STATISTIC_ID")
    @SequenceGenerator(name = "STATISTICS_SEQ", sequenceName = "STATISTICS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATISTICS_SEQ")
    private Long statisticId;

    @Basic
    @Column(name = "STATISTIC_MARK", nullable = false)
    private String statisticMark;

    @Basic
    @Column(name = "PAGE_NAME", nullable = false)
    private String pageName;

    @Basic
    @Column(name = "IP_ADDRESS", unique = true, nullable = false)
    private String ipAddress;

    @Basic
    @Column(name = "BROWSER_INFO", nullable = false)
    private String browserInfo;

    @Basic
    @Column(name = "VISIT_DATE", nullable = false)
    private LocalDate visitDate;

    @Basic
    @Column(name = "COOKIES", nullable = false)
    private String cookies;

    @Basic
    @Column(name = "REQUESTED_PARAMS", nullable = false)
    private String requestedParams;

    @OneToOne
    @JoinColumn(name = "PREV_STATISTIC_ID", foreignKey = @ForeignKey(name = "PREV_STATISTIC_FK"))
    private StatisticEntity prevStatisticEntity;

}
