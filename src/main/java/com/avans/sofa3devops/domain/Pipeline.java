package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domain.action.Deploy;
import com.avans.sofa3devops.domain.tools.AssemblyScanner;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.IPipelineState;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.InitialState;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Pipeline implements IPipeComponent {
    private final String name;
    private IPipelineState state;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private List<IPipeComponent> allCommands;
    private List<IPipeComponent> selectedCommands;
    private ISprint sprint;

    public Pipeline(String name, ISprint sprint) throws Exception {
        this.name = name;
        this.state = new InitialState(this, new NotificationService(new NotificationExecutor()));
        this.allCommands = new ArrayList<>();
        this.selectedCommands = new ArrayList<>();
        this.sprint = sprint;

        initAllCommands();
        initSelectedCommands();
    }

    private void initAllCommands() throws Exception {
        List<Command> commands = new ArrayList<>();
        List<Action> actions = new ArrayList<>();

        for (Class<?> commandClass : AssemblyScanner.getAllClasses("com.avans.sofa3devops.domain.command")) {
            try {
                Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
                commands.add(command);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        for (Class<?> actionClass : AssemblyScanner.getAllClasses("com.avans.sofa3devops.domain.action")) {
            try {
                Action action = (Action) actionClass.getDeclaredConstructor().newInstance();
                actions.add(action);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        Collections.sort(actions, Comparator.comparingInt(Action::getIndex));

        for (var action : actions) {
            commands.stream()
                    .filter(a -> a.getAction().getClass() == action.getClass())
                    .forEach(action::add);

            allCommands.add(action);
        }
    }

    private void initSelectedCommands() throws Exception {
        List<Action> actions = new ArrayList<>();

        for (Class<?> actionClass : AssemblyScanner.getAllClasses("com.avans.sofa3devops.domain.action")) {
            try {
                Action action = (Action) actionClass.getDeclaredConstructor().newInstance();
                actions.add(action);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        Collections.sort(actions, Comparator.comparingInt(Action::getIndex));

        selectedCommands.addAll(actions);
    }

    public void addCommandToAction(Command command) {
        var action = (Action) selectedCommands.stream().filter(a -> a.getClass() == command.getAction().getClass()).toList().getFirst();
        action.add(command);
    }

    public void removeCommandToAction(Command command) {
        var action = (Action) selectedCommands.stream().filter(a -> a.getClass() == command.getAction().getClass()).toList().getFirst();
        action.remove(command);
    }

    public IPipelineState getState() {
        return state;
    }

    public void setState(IPipelineState state) {
        this.state = state;
    }

    // Pipeline controller methods
    public void executedState() throws InvalidStateException {
        this.state.executedState();
    }

    public void finishedState() throws InvalidStateException {
        this.state.finishedState();
    }

    public void failedState() throws InvalidStateException {
        this.state.failedState();
    }

    public void cancelledState() throws InvalidStateException {
        this.state.cancelledState();
    }

    @Override
    public void print() {
        for (IPipeComponent action : allCommands) {
            action.print();
        }
    }

    @Override
    public boolean execute() throws InvalidStateException {
        this.executedState();

        boolean containsDeploy = false;
        for (IPipeComponent component : selectedCommands) {
            if (component instanceof Deploy) {
                containsDeploy = true;
                break;
            }
        }

        if (!containsDeploy) {
            logger.info("The pipeline contains no deploy action!");
            return false;
        }

        for (IPipeComponent action : selectedCommands) {
            Action comp = (Action) action;
            if (comp.getCommands().isEmpty()) {
                logger.info("Pipeline action '" + action.getClass().getSimpleName() + "' failed, check configuration!");
                return false;
            }

            boolean successful = action.execute();
            if (!successful) {
                return false;
            }
        }
        return true;
    }

    public ISprint getSprint() {
        return this.sprint;
    }
}
