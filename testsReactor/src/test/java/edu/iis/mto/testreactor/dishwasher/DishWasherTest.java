package edu.iis.mto.testreactor.dishwasher;

import static edu.iis.mto.testreactor.dishwasher.Status.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
		assertEquals(successWashing(),result);
	}

	private RunResult successWashing() {
		return RunResult.builder()
				.withStatus(SUCCESS)
				.withRunMinutes(properProgramConfiguration.getProgram().getTimeInMinutes())
				.build();
	}

	private ProgramConfiguration getProperProgramConfiguration() {
		return ProgramConfiguration.builder()
				.withProgram(WashingProgram.ECO)
				.withTabletsUsed(false)
				.withFillLevel(FillLevel.HALF)
				.build();
	}

}
