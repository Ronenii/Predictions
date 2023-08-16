package manager;

import engine2ui.simulation.start.StartData;
import ui2engine.simulation.func3.DTOThirdFunction;

import engine2ui.simulation.result.ResultData;
import ui2engine.simulation.func1.DTOFirstFunction;

public interface EngineInterface {
    String getCurrentSimulationDetails();

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    void loadSimulationFromFile(DTOFirstFunction dto);

    void runSimulation(DTOThirdFunction dtoThirdFunction);

    StartData getSimulationStartData();
}
