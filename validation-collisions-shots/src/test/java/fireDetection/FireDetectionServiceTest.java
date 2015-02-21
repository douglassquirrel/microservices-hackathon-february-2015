package fireDetection;

import colisionDetection.BotPosition;
import colisionDetection.BotPositionsMessage;
import colisionDetection.CollisionDetectionService;
import colisionDetection.ScoreEventHandler;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FireDetectionServiceTest {

    @Mock
    private ScoreEventHandler scoreEventHandler;

    private FireDetectionService fireDetectionService;

    @Before
    public void setUp() throws Exception {
        fireDetectionService = new FireDetectionService(scoreEventHandler);
    }

    @Test
    public void shouldFire() throws Exception {
        fireDetectionService.updateState(new BotPositionsMessage(Lists.newArrayList(
                new BotPosition("player1", 5, 5),
                new BotPosition("player2", 5, 3),
                new BotPosition("player3", 5, 1),
                new BotPosition("player4", 5, 0)
        )));

        // When
        fireDetectionService.fire("player3");

        // Then
        verify(scoreEventHandler).handleScoreEvent("player1");
        verify(scoreEventHandler).handleScoreEvent("player2");
        verify(scoreEventHandler, never()).handleScoreEvent("player3");
        verify(scoreEventHandler, never()).handleScoreEvent("player4");

    }
}
