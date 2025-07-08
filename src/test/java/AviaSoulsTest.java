import org.junit.jupiter.api.Test;

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

        assertEquals(3, result.length);
        assertEquals(3000, result[0].getPrice());
        assertEquals(4000, result[1].getPrice());
        assertEquals(5000, result[2].getPrice());
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

        assertEquals(3, result.length);
        assertEquals(3000, result[0].getPrice()); // 2ч
        assertEquals(4000, result[1].getPrice()); // 3ч
        assertEquals(5000, result[2].getPrice()); // 4ч
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
    public void testSearchWhenNoTicketsInSystem() {
        AviaSouls souls = new AviaSouls();
        Ticket[] result = souls.search("MOW", "SPB");
        assertEquals(0, result.length);
    }

    @Test
    public void testTicketEqualsSameObject() {
        Ticket t = new Ticket("MOW", "SPB", 3000, 10, 12);
        assertEquals(t, t);
    }

    @Test
    public void testTicketEqualsNull() {
        Ticket t = new Ticket("MOW", "SPB", 3000, 10, 12);
        assertNotEquals(t, null);
    }

    @Test
    public void testTicketEqualsDifferentClass() {
        Ticket t = new Ticket("MOW", "SPB", 3000, 10, 12);
        Object other = new Object();
        assertNotEquals(t, other);
    }

    @Test
    public void testSearchWithOneResult() {
        AviaSouls souls = new AviaSouls();
        Ticket t = new Ticket("MOW", "SPB", 4000, 10, 12);
        souls.add(t);

        Ticket[] result = souls.search("MOW", "SPB");
        assertArrayEquals(new Ticket[]{t}, result);
    }

    @Test
    public void testTicketHashCodeIsConsistent() {
        Ticket t = new Ticket("MOW", "SPB", 3000, 10, 12);
        int hash1 = t.hashCode();
        int hash2 = t.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testTicketNotEqualsIfDifferentFields() {
        Ticket t1 = new Ticket("MOW", "SPB", 3000, 10, 12);
        Ticket t2 = new Ticket("MOW", "SPB", 3500, 10, 12); // другая цена
        assertNotEquals(t1, t2);
    }

    @Test
    public void testSearchWithEmptyTicketList() {
        AviaSouls souls = new AviaSouls();
        Ticket[] result = souls.search("MOW", "SPB");
        assertEquals(0, result.length);
    }

    @Test
    public void testSearchOnlyExactMatchesIncluded() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12)); // ← подходит
        souls.add(new Ticket("MOW", "KZN", 4000, 11, 13)); // ← не подходит
        souls.add(new Ticket("SPB", "MOW", 5000, 14, 16)); // ← не подходит

        Ticket[] result = souls.search("MOW", "SPB");
        assertEquals(1, result.length);
        assertEquals("SPB", result[0].getTo());
    }

    @Test
    public void testSearchAndSortBySingleResultWithComparator() {
        AviaSouls souls = new AviaSouls();
        Ticket t = new Ticket("MOW", "SPB", 4500, 10, 13);
        souls.add(t);

        Ticket[] result = souls.searchAndSortBy("MOW", "SPB", (a, b) -> Integer.compare(a.getPrice(), b.getPrice()));
        assertArrayEquals(new Ticket[]{t}, result);
    }

    @Test
    public void testSearchAndSortByPriceComparatorLambda() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 7000, 10, 12));
        souls.add(new Ticket("MOW", "SPB", 2000, 11, 13));
        souls.add(new Ticket("MOW", "SPB", 5000, 12, 14));

        Ticket[] result = souls.searchAndSortBy("MOW", "SPB", Comparator.comparingInt(Ticket::getPrice));

        assertEquals(3, result.length);
        assertEquals(2000, result[0].getPrice());
        assertEquals(5000, result[1].getPrice());
        assertEquals(7000, result[2].getPrice());
    }

    @Test
    public void testSearchDifferentRoutesMixedTickets() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        souls.add(new Ticket("SPB", "KZN", 4000, 11, 13));
        souls.add(new Ticket("MOW", "KZN", 5000, 12, 14));

        Ticket[] result1 = souls.search("MOW", "SPB");
        assertEquals(1, result1.length);
        assertEquals("SPB", result1[0].getTo());

        Ticket[] result2 = souls.search("SPB", "KZN");
        assertEquals(1, result2.length);
        assertEquals("KZN", result2[0].getTo());

        Ticket[] result3 = souls.search("MOW", "KZN");
        assertEquals(1, result3.length);
        assertEquals("KZN", result3[0].getTo());
    }

    @Test
    public void testAddArrayViaSearchAndSortByMultipleAdds() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        souls.add(new Ticket("MOW", "SPB", 4000, 11, 13));
        souls.add(new Ticket("MOW", "SPB", 5000, 12, 14));

        Ticket[] result = souls.searchAndSortBy("MOW", "SPB", new TicketTimeComparator());
        assertEquals(3, result.length);
    }

    @Test
    public void testFindAllAfterMultipleAdds() {
        AviaSouls souls = new AviaSouls();
        Ticket[] expected = new Ticket[]{
                new Ticket("MOW", "SPB", 3000, 10, 12),
                new Ticket("SPB", "KZN", 4000, 11, 13),
                new Ticket("MOW", "KZN", 5000, 12, 14)
        };
        for (Ticket t : expected) {
            souls.add(t);
        }

        Ticket[] all = souls.findAll();
        assertArrayEquals(expected, all);
    }

    @Test
    public void testSearchAndSortByEmptyAfterMixedAdds() {
        AviaSouls souls = new AviaSouls();
        souls.add(new Ticket("MOW", "SPB", 3000, 10, 12));
        souls.add(new Ticket("SPB", "MOW", 3500, 13, 15));

        Ticket[] result = souls.searchAndSortBy("KZN", "SPB", new TicketTimeComparator());
        assertNotNull(result);
        assertEquals(0, result.length);
    }
}