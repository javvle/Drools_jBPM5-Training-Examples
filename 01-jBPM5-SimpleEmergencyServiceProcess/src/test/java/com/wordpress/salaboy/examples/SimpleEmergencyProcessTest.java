package com.wordpress.salaboy.examples;

import com.wordpress.salaboy.example.model.Emergency;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleEmergencyProcessTest {
    private static StatefulKnowledgeSession ksession;

    public SimpleEmergencyProcessTest() {
    }

    /*
     * This test shows how to interact with a business process that contains
     * human tasks activities using an Automatic Human Task Activities simulator. This business process
     * also contains a BusinessRuleTask activity defined. A set of rules will be evaluated when this activity is
     * executed by the process. Take a look at the comments in the test for more explanations.
     * When a Human Task is found inside the business process, the  MyHumanActivitySimulatorWorkItemHandler
     * automatically completes the task causing the process to advance to the next activity.
     */
    @Test
    public void emergencyServiceAutomaticHumanTasksTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanActivityAutomaticSimulatorWorkItemHandler humanActivitiesSimHandler = new MyHumanActivityAutomaticSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        //Setting the process engine and the rule engine in reactive mode
        // This will cause that if a rule is activated, the rule will fire without waiting
        // the user to call the fireAllRules() method.
        new Thread(new Runnable() {

            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();

        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService");

        Thread.sleep(1000);

        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());


    }

    /*
     * This test shows how to interact with a business process that contains
     * human tasks activities using an Automatic Human Task Activities simulator. This business process
     * also contains a BusinessRuleTask activity defined. A set of rules will be evaluated when this activity is
     * executed by the process. Take a look at the comments in the test for more explanations.
     * When a Human Task is found inside the business process, the  MyHumanActivitySimulatorWorkItemHandler
     * automatically completes the task causing the process to advance to the next activity.
     */
    @Test
    public void emergencyServiceNonAutomaticHumanTasksTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanActivityNonAutomaticSimulatorWorkItemHandler humanActivitiesSimHandler = new MyHumanActivityNonAutomaticSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        //Setting the process engine and the rule engine in reactive mode
        // This will cause that if a rule is activated, the rule will fire without waiting
        // the user to call the fireAllRules() method.
        new Thread(new Runnable() {

            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();

        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService");


        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        System.out.println("Completing the first Activity");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem(null);
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Thread.sleep(1000);
        System.out.println("Completing the second Activity");
        //Complete the second human activity
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        Thread.sleep(1000);
        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        humanActivitiesSimHandler.completeWorkItem(null);


        Thread.sleep(1000);

        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());
    }

    @Test
    public void emergencyServiceWithInputDataTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanActivitySimulatorChangeValuesWorkItemHandler humanActivitiesSimHandler = new MyHumanActivitySimulatorChangeValuesWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        //Setting the process engine and the rule engine in reactive mode
        // This will cause that if a rule is activated, the rule will fire without waiting
        // the user to call the fireAllRules() method.
        new Thread(new Runnable() {

            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", new Emergency("555-1234"));


        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);

        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        Assert.assertEquals(1, ((Emergency) process.getVariable("emergency")).getRevision());
        System.out.println("Completing the first Activity");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem();
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Thread.sleep(1000);
        System.out.println("Completing the second Activity");
        //Complete the second human activity
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        Thread.sleep(1000);
        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        Assert.assertEquals(2, ((Emergency) process.getVariable("emergency")).getRevision());
        humanActivitiesSimHandler.completeWorkItem();


        Thread.sleep(1000);
    }

    @Test
    public void emergencyServiceWithRulesTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("SelectEmergencyVehicleSimple.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanActivitySimulatorChangeValuesWorkItemHandler humanActivitiesSimHandler = new MyHumanActivitySimulatorChangeValuesWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        //Setting the process engine and the rule engine in reactive mode
        // This will cause that if a rule is activated, the rule will fire without waiting
        // the user to call the fireAllRules() method.
        new Thread(new Runnable() {

            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();
        Emergency emergency = new Emergency("555-1234");
        emergency.setType("Heart Attack");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", emergency);


        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        ksession.insert(emergency);
        ksession.insert(process);

        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        Assert.assertEquals(1, ((Emergency) process.getVariable("emergency")).getRevision());
        System.out.println("Completing the first Activity");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem();
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Thread.sleep(1000);

        System.out.println("Completing the second Activity");
        //Complete the second human activity
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        Thread.sleep(1000);
        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        Assert.assertEquals(2, ((Emergency) process.getVariable("emergency")).getRevision());
        humanActivitiesSimHandler.completeWorkItem();


        Thread.sleep(1000);

    }


    @Test
    public void emergencyServiceWithRulesNoReactiveTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("SelectEmergencyVehicleSimple.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanActivitySimulatorChangeValuesWorkItemHandler humanActivitiesSimHandler = new MyHumanActivitySimulatorChangeValuesWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);


        Emergency emergency = new Emergency("555-1234");
        //Run with Heart Attack and check the output. An Ambulance must appear in the report
        //emergency.setType("Heart Attack");
        //Run with Fire and check the output. A FireTruck must appear in the report
        emergency.setType("Fire");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", emergency);


        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        ksession.insert(emergency);
        ksession.insert(process);

        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        Assert.assertEquals(1, ((Emergency) process.getVariable("emergency")).getRevision());
        System.out.println("Completing the first Activity");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem();
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        Thread.sleep(1000);
        // At this point we need to fireAllRules() that were activated
        int fired = ksession.fireAllRules();
        Assert.assertEquals(1, fired);
        Thread.sleep(1000);

        System.out.println("Completing the second Activity");
        //Complete the second human activity
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        Thread.sleep(1000);
        Assert.assertEquals(1, process.getNodeInstances().size());
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        Assert.assertEquals(2, ((Emergency) process.getVariable("emergency")).getRevision());
        humanActivitiesSimHandler.completeWorkItem();


        Thread.sleep(1000);

    }


}

class MyHumanActivityNonAutomaticSimulatorWorkItemHandler implements WorkItemHandler {
    private WorkItemManager workItemManager;
    private long workItemId;

    public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        this.workItemId = workItem.getId();
        this.workItemManager = workItemManager;
        System.out.println("Map of Parameters = " + workItem.getParameters());
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }

    public void completeWorkItem(Map<String, Object> parameters) {
        this.workItemManager.completeWorkItem(this.workItemId, parameters);

    }

}


class MyHumanActivitySimulatorChangeValuesWorkItemHandler implements WorkItemHandler {
    private static int counter = 1;
    private WorkItemManager workItemManager;
    private long workItemId;
    private Map<String, Object> results;
    private Emergency currentEmergency;

    public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        this.workItemId = workItem.getId();
        this.workItemManager = workItemManager;
        currentEmergency = (Emergency) workItem.getParameter("emergency");
        currentEmergency.setRevision(currentEmergency.getRevision() + counter);


    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }

    public void completeWorkItem() {
        results = new HashMap<String, Object>();
        results.put("emergency", currentEmergency);
        workItemManager.completeWorkItem(workItemId, results);

    }


}

class MyHumanActivityAutomaticSimulatorWorkItemHandler implements WorkItemHandler {

    public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        System.out.println("Map of Parameters = " + workItem.getParameters());
        workItemManager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }


}
