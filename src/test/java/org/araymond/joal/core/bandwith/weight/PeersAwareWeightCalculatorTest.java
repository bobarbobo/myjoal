package org.araymond.joal.core.bandwith.weight;

import org.araymond.joal.core.bandwith.Peers;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PeersAwareWeightCalculatorTest {

    @Test
    public void shouldNeverGoBelowZero() {
        final PeersAwareWeightCalculator calculator = new PeersAwareWeightCalculator();
        assertThat(calculator.calculate(new Peers(0, 0),"dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(0, 1),"dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(1, 0),"dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(200, 100),"HRdummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(200, 100),"HR dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(200, 100),"dummyHR")).isNotEqualTo(0);
    }

    @Test
    public void shouldPromoteTorrentWithMoreLeechers() {
        final PeersAwareWeightCalculator calculator = new PeersAwareWeightCalculator();

        final double first = calculator.calculate(new Peers(10, 10),"dummy");
        final double second = calculator.calculate(new Peers(10, 30),"dummy");
        final double third = calculator.calculate(new Peers(10, 100),"dummy");
        final double fourth = calculator.calculate(new Peers(10, 200),"dummy");

        assertThat(fourth)
                .isGreaterThan(third)
                .isGreaterThan(second)
                .isGreaterThan(first);
    }

    @Test
    public void shouldProvideExactValues() {
        final PeersAwareWeightCalculator calculator = new PeersAwareWeightCalculator();

        assertThat(calculator.calculate(new Peers(1, 1),"dummy")).isEqualTo(25);
        assertThat(calculator.calculate(new Peers(2, 1),"dummy")).isCloseTo(11.1, Offset.offset(0.1));
        assertThat(calculator.calculate(new Peers(30, 1),"dummy")).isCloseTo(0.104058273, Offset.offset(0.00000001));
        assertThat(calculator.calculate(new Peers(0, 1),"dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(1, 0),"dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(2, 100),"dummy")).isCloseTo(9611.687812, Offset.offset(0.0001));
        assertThat(calculator.calculate(new Peers(0, 100),"dummy")).isEqualTo(0);
        assertThat(calculator.calculate(new Peers(2000, 150),"dummy")).isEqualTo(73.01243916, Offset.offset(0.00001));
        assertThat(calculator.calculate(new Peers(150, 2000),"dummy")).isCloseTo(173066.5224, Offset.offset(0.01));
        assertThat(calculator.calculate(new Peers(80, 2000),"dummy")).isCloseTo(184911.2426, Offset.offset(0.1));
        assertThat(calculator.calculate(new Peers(2000, 2000),"dummy")).isEqualTo(50000);
        assertThat(calculator.calculate(new Peers(0, 0),"dummy")).isEqualTo(0);
    }

}
