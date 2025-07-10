import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class AviaSoulsTest {

    @Test
    public void testCompareToLowerPrice() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12);
        Ticket t2 = new Ticket("MOW", "SPB", 5000, 11, 13);
        assertTrue(t1.compareTo(t2) < 0);
    }

    @Test
    public void testCompareToHigherPrice() {
        Ticket t1 = new Ticket("MOW", "SPB", 6000, 10, 12);
        Ticket t2 = new Ticket("MOW", "SPB", 4000, 11, 13);
        assertTrue(t1.compareTo(t2) > 0);
    }

    @Test
    public void testCompareToEqualPrices() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12);
        Ticket t2 = new Ticket("MOW", "SPB", 3000, 11, 13);
        assertEquals(0, t1.compareTo(t2));
    }

    @Test
    public void testTicketEqualsAndHashCode() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12);
        Ticket t2 = new Ticket("MOW", "SPB", 3000, 10, 12);
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testTicketNotEquals() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12);
        Ticket t2 = new Ticket("SPB", "KZN", 4000, 15, 18);
        assertNotEquals(t1, t2);
    }

    @Test
    public void testSearchSortedByPrice() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 5000, 10, 12));
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        souls.add(new Ticket("MOW", "SPB", 4000, 10, 12));

        Ticket[] result = souls.search("MOW", "SPB");

        int[] expectedPrices = {3000, 4000, 5000};
        int[] actualPrices = Arrays.stream(result).mapToInt(Ticket::getPrice).toArray();
        assertArrayEquals(expectedPrices, actualPrices);
    }

    @Test
    public void testSearchReturnsEmptyIfNoMatch() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        Ticket[] result = souls.search("SPB", "MOW");
        assertEquals(0, result.length);
    }

    @Test
    public void testSearchAndSortByDuration() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 5000, 10, 14)); // 4ч
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12)); // 2ч
        souls.add(new Ticket("MOW", "SPB", 4000, 10, 13)); // 3ч

        Ticket[] result = souls.searchAndSortBy("MOW", "SPB", new TicketTimeComparator());

        int[] expectedPrices = {3000, 4000, 5000};
        int[] actualPrices = Arrays.stream(result).mapToInt(Ticket::getPrice).toArray();
        assertArrayEquals(expectedPrices, actualPrices);
    }

    @Test
    public void testSearchAndSortByReturnsEmptyIfNoMatch() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        Ticket[] result = souls.searchAndSortBy("SPB", "KZN", new TicketTimeComparator());
        assertEquals(0, result.length);
    }

    @Test
    public void testFindAllReturnsAllTickets() {
        AviaSouls souls = new AviaSouls();
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12);
        Ticket t2 = new Ticket("SPB", "KZN", 5000, 13, 17);
        souls.add(t1);
        souls.add(t2);
        Ticket[] result = souls.findAll();
        assertArrayEquals(new Ticket[]{t1, t2}, result);
    }

    @Test
    public void testAddSingleTicket() {
        AviaSouls souls = new AviaSouls();
        Ticket t = new Ticket("MOW", "SPB", 3000, 10, 12);
        souls.add(t);
        Ticket[] all = souls.findAll();
        assertEquals(1, all.length);
        assertEquals(t, all[0]);
    }

    @Test
    public void testTicketTimeComparatorLess() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12); // 2ч
        Ticket t2 = new Ticket("MOW", "SPB", 3000, 10, 14); // 4ч
        Comparator<Ticket> comparator = new TicketTimeComparator();
        assertTrue(comparator.compare(t1, t2) < 0);
    }

    @Test
    public void testTicketTimeComparatorMore() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 15); // 5ч
        Ticket t2 = new Ticket("MOW", "SPB", 3000, 10, 13); // 3ч
        Comparator<Ticket> comparator = new TicketTimeComparator();
        assertTrue(comparator.compare(t1, t2) > 0);
    }

    @Test
    public void testTicketTimeComparatorEqualDurations() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12); // 2ч
        Ticket t2 = new Ticket("MOW", "SPB", 3500, 11, 13); // 2ч
        Comparator<Ticket> comparator = new TicketTimeComparator();
        assertEquals(0, comparator.compare(t1, t2));
    }

    @Test
    public void testSearchAndSortBySingleResultWithComparator() {
        AviaSouls souls = new AviaSouls();
        Ticket t = new Ticket("MOW", "SPB", 4500, 10, 13);
        souls.add(t);
        Ticket[] result = souls.searchAndSortBy("MOW", "SPB", Comparator.comparingInt(Ticket::getPrice));
        assertArrayEquals(new Ticket[]{t}, result);
    }

    @Test
    public void testSearchAndSortByPriceComparatorLambda() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 7000, 10, 12));
        souls.add(new Ticket("MOW", "SPB", 2000, 11, 13));
        souls.add(new Ticket("MOW", "SPB", 5000, 12, 14));

        Ticket[] result = souls.searchAndSortBy("MOW", "SPB", Comparator.comparingInt(Ticket::getPrice));

        int[] expected = {2000, 5000, 7000};
        int[] actual = Arrays.stream(result).mapToInt(Ticket::getPrice).toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchDifferentRoutesMixedTickets() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        souls.add(new Ticket("SPB", "KZN", 4000, 11, 13));
        souls.add(new Ticket("MOW", "KZN", 5000, 12, 14));

        Ticket[] result1 = souls.search("MOW", "SPB");
        assertArrayEquals(new Ticket[]{new Ticket("MOW", "SPB", 3000, 10, 12)}, result1);

        Ticket[] result2 = souls.search("SPB", "KZN");
        assertArrayEquals(new Ticket[]{new Ticket("SPB", "KZN", 4000, 11, 13)}, result2);

        Ticket[] result3 = souls.search("MOW", "KZN");
        assertArrayEquals(new Ticket[]{new Ticket("MOW", "KZN", 5000, 12, 14)}, result3);
    }
}