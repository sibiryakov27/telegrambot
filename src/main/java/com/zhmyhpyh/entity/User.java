package com.zhmyhpyh.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "tg_data")
public class User {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "msg_number_total")
    private Integer totalMessageNumber;

    @Column(name = "msg_number_daily")
    private Integer dailyMessageNumber;

    @Column(name = "start_count_date")
    private Date date;

}
