package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Reading employee[{}]", id);

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(id);
        int numberOfReports = 0;

        // Get employee information
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee.getDirectReports() != null) {
            // Get list of direct reports and check to see if they have any direct reports
            String directReport = null;
            for (int i = 0; i < employee.getDirectReports().size(); i++) {
                directReport = employee.getDirectReports().get(i).getEmployeeId();
                numberOfReports++;
                // Check to see if the direct reports has direct reports
                if (employeeRepository.findByEmployeeId(directReport).getDirectReports() != null) {
                    for (int x = 0; x < employeeRepository.findByEmployeeId(directReport).getDirectReports().size(); x++) {
                        numberOfReports++;
                    }
                }
            }
        }

        reportingStructure.setNumberOfReports(Integer.toString(numberOfReports));
        return reportingStructure;
    }
}