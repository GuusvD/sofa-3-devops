package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.*;

import java.util.Date;
import java.util.List;

public class ReviewSprint implements ISprint {
    private int number;
    private Date start;
    private Date end;
    private List<BacklogItem> backlog;
    private List<User> developers;
    private Document document;
    private Release release;
    private Pipeline pipeline;
    private boolean reviewed;
}
