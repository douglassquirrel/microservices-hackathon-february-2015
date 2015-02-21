package colisionDetection;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CollisionDetectionServiceTest {

    @Mock
    private ScoreEventHandler scoreEventHandler;

    private CollisionDetectionService collisionDetectionService;

    @Before
    public void setUp() throws Exception {
        collisionDetectionService = new CollisionDetectionService(scoreEventHandler);
    }

    @Test
    public void shouldIgnoreWhenNoCollision() {
        // Given
        BotPositionsMessage positionsMessage = new BotPositionsMessage(Lists.newArrayList(
                new BotPosition("test1", 1, 1),
                new BotPosition("test2", 2, 1)
        ));

        // When
        collisionDetectionService.detectCollisions(positionsMessage);

        // Then
        verifyNoMoreInteractions(scoreEventHandler);
    }

    @Test
    public void shouldDetectCollision() {
        // Given
        BotPositionsMessage positionsMessage = new BotPositionsMessage(Lists.newArrayList(
                new BotPosition("test1", 1, 1),
                new BotPosition("test2", 1, 1)
        ));

        // When
        collisionDetectionService.detectCollisions(positionsMessage);

        // Then
        verify(scoreEventHandler).handleScoreEvent("test1");
        verify(scoreEventHandler).handleScoreEvent("test2");
    }
}
