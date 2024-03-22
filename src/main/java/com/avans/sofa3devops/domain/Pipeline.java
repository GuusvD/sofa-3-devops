package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domain.action.Deploy;
import com.avans.sofa3devops.domain.tools.AssemblyScanner;
import com.avans.sofa3devops.domainservices.compositeinterfaces.IPipeComponent;
import com.avans.sofa3devops.domainservices.exceptions.AssemblyException;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.exceptions.PipelineException;
import com.avans.sofa3devops.domainservices.pipelinestatepattern.IPipelineState;
import com.avans.sofa3devops.domainservices.pipelinestatepattern.InitialState;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

import java.util.*;
import java.util.logging.Logger;

public class Pipeline implements IPipeComponent {
    private final String name;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private IPipelineState state;
    private List<IPipeComponent> allCommands;
    private List<IPipeComponent> selectedCommands;
    private ISprint sprint;

    public Pipeline(String name, ISprint sprint) throws PipelineException, AssemblyException {
        this.name = name;
        this.state = new InitialState(this, new NotificationService(new NotificationExecutor()));
        this.allCommands = new ArrayList<>();
        this.selectedCommands = new ArrayList<>();
        this.sprint = sprint;

        initAllCommands();
        initSelectedCommands();
    }

    private void initAllCommands() throws PipelineException, AssemblyException {
        List<Command> commands = new ArrayList<>();
        List<Action> actions = new ArrayList<>();

        for (Class<?> commandClass : AssemblyScanner.getAllClasses("com.avans.sofa3devops.domain.command")) {
            try {
                Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
                commands.add(command);
            } catch (Exception e) {
                throw new PipelineException(e.getMessage());
            }
        }

        for (Class<?> actionClass : AssemblyScanner.getAllClasses("com.avans.sofa3devops.domain.action")) {
            try {
                Action action = (Action) actionClass.getDeclaredConstructor().newInstance();
                actions.add(action);
            } catch (Exception e) {
                throw new PipelineException(e.getMessage());
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

    private void initSelectedCommands() throws PipelineException, AssemblyException {
        List<Action> actions = new ArrayList<>();

        for (Class<?> actionClass : AssemblyScanner.getAllClasses("com.avans.sofa3devops.domain.action")) {
            try {
                Action action = (Action) actionClass.getDeclaredConstructor().newInstance();
                actions.add(action);
            } catch (Exception e) {
                throw new PipelineException(e.getMessage());
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

    public String getName() {
        return name;
    }
}
