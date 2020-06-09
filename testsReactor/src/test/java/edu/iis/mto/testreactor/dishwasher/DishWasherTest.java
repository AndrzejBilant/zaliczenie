package edu.iis.mto.testreactor.dishwasher;

import static edu.iis.mto.testreactor.dishwasher.Status.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.engine.EngineException;
import edu.iis.mto.testreactor.dishwasher.pump.PumpException;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DishWasherTest {

	@Mock
	private WaterPump waterPump;
	@Mock
	private Engine engine;
	@Mock
	private DirtFilter dirtFilter;
	@Mock
	private Door door;

	private DishWasher dishWasher;
	private ProgramConfiguration properProgramConfiguration;

	@BeforeEach
	void setUp() {
		dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
		properProgramConfiguration = getProperProgramConfiguration();
	}

	@Test
	public void properWashingDishesWithStaticProgramWithOpenDoorShouldResultInDOOR_OPENError() {
		RunResult result = dishWasher.start(properProgramConfiguration);
		assertEquals(error(Status.DOOR_OPEN), result);
	}

	@Test
	public void properWashingDishesWithStaticProgramWithFilterProblemShouldResultInERROR_FILTER() {
		Mockito.when(door.closed()).thenReturn(true);
		Mockito.when(dirtFilter.capacity()).thenReturn(dishWasher.MAXIMAL_FILTER_CAPACITY - 1);
		RunResult result = dishWasher.start(properProgramConfiguration);
		assertEquals(error(Status.ERROR_FILTER), result);
	}



	@Test
	public void properWashingDishesWithStaticProgramShouldResultInSuccess() {
		Mockito.when(door.closed()).thenReturn(true);
		Mockito.when(dirtFilter.capacity()).thenReturn(dishWasher.MAXIMAL_FILTER_CAPACITY + 1);
		RunResult result = dishWasher.start(properProgramConfiguration);
		assertEquals(successWashing(), result);

	}

	@Test
	public void properWashingDishesWithStaticProgramShouldCallDoorDirtFilterWaterPumpAndEngine() throws PumpException, EngineException {
		Mockito.when(door.closed()).thenReturn(true);
		Mockito.when(dirtFilter.capacity()).thenReturn(dishWasher.MAXIMAL_FILTER_CAPACITY + 1);
		dishWasher.start(properProgramConfiguration);


		InOrder callOrder = Mockito.inOrder(door, dirtFilter, waterPump, engine, waterPump, door);
		callOrder.verify(door).closed();
		callOrder.verify(dirtFilter).capacity();
		callOrder.verify(waterPump).pour(FillLevel.HALF);
		callOrder.verify(engine).runProgram(properProgramConfiguration.getProgram());
		callOrder.verify(waterPump).drain();
		callOrder.verify(door).unlock();


	}


	private ProgramConfiguration getProperProgramConfiguration() {
		return ProgramConfiguration.builder()
				.withProgram(WashingProgram.ECO)
				.withTabletsUsed(true)
				.withFillLevel(FillLevel.HALF)
				.build();
	}

	private RunResult error(Status errorPump) {
		return RunResult.builder()
				.withStatus(errorPump)
				.build();
	}

	private RunResult successWashing() {
		return RunResult.builder()
				.withStatus(SUCCESS)
				.withRunMinutes(properProgramConfiguration.getProgram().getTimeInMinutes())
				.build();
	}

}
