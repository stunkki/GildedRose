package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

// Example scenarios for testing
//   Item("+5 Dexterity Vest", 10, 20));
//   Item("Aged Brie", 2, 0));
//   Item("Elixir of the Mongoose", 5, 7));
//   Item("Sulfuras, Hand of Ragnaros", 0, 80));
//   Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
//   Item("Conjured Mana Cake", 3, 6));

	@Test
	public void testUpdateEndOfDay_AgedBrie_memberVariables() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 10) );
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals("Name does not change", "Aged Brie", itemBrie.getName());
		assertEquals("Quality rises 1 end of the day", 11, itemBrie.getQuality());
		assertEquals("SellIn should drop by one", 1, itemBrie.getSellIn());
	}
    
	@Test
	public void testUpdateEndOfDay_AgedBrie_tooHighQuality() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 50) );
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals("Quality should not rise over 50", 50, itemBrie.getQuality());
	}
	
	@Test
	public void testUpdateAfter2Weeks_DexVest() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("+5 Dexterity Vest", 4, 40));
		
		// Act
		for (int i = 0; i < 14; i++) {
			store.updateEndOfDay();
		}


		List<Item> items = store.getItems();
		Item itemDexVest = items.get(0);
		
		// Assert
		assertEquals("Quality should drop twice as fast after 4 days: 40-4-10*2=16", 16, itemDexVest.getQuality());
		assertEquals("Sell in should be 4-14 = -10, not sell in for 10 days", -10, itemDexVest.getSellIn());
		assertEquals("Name should be still: '+5 Dexterity Vest'", "+5 Dexterity Vest", itemDexVest.getName());
	}
	
	@Test
	public void testUpdateAfterTwoDays_qualityCannotBeNegative() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("+5 Dexterity Vest", 2, 1));
		
		// Act
		store.updateEndOfDay();
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item dexVest = items.get(0);
		assertEquals("Quality cannot be negative", 0, dexVest.getQuality());
		assertEquals("SellIn should be 0", 0, dexVest.getSellIn());
		assertEquals("Name should be '+5 Dexterity Vest'", "+5 Dexterity Vest", dexVest.getName());
	}
	
	@Test
	public void testUpdateEndOfTheDay_BackstagePassQualityRise2WhenSellIn3To10Days() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 5));
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item pass = items.get(0);
		assertEquals("Quality should rise by 2: 5+2=7", 7, pass.getQuality());
		assertEquals("SellIn Should be 10-1=9", 9, pass.getSellIn());
	}
	
	@Test
	public void testUpdateEndOfTheDay_BackstagePassQualityRise1WhenSellInOver10Days() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 12, 1));
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item pass = items.get(0);
		assertEquals("Quality should rise by 1: 1+1=2", 2, pass.getQuality());
		assertEquals("SellIn Should be 11", 11, pass.getSellIn());
	}
	
	@Test
	public void testUpdateEndOfTheDay_BackstagePassQualityRise3WhenSellIn3To5Days() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 5));
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item pass = items.get(0);
		assertEquals("Quality should rise by 3: 5+3=8", 8, pass.getQuality());
		assertEquals("SellIn Should be 5", 4, pass.getSellIn());
	}
	
	@Test
	public void testUpdateEndOfTheDay_BackstagePassQuality0WhenSellInNegative() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 0, 5));
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item pass = items.get(0);
		assertEquals("Quality should be 0", 0, pass.getQuality());
		assertEquals("SellIn Should be -1", -1, pass.getSellIn());
	}
	
	@Test
	public void testUpdateEndOfDay_ConjuredManaCake() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Conjured Mana Cake", 3, 8) );
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item manaCake = items.get(0);
		assertEquals("Conjured Mana Cake", manaCake.getName());
		assertEquals("Conjured Mana cakes sell in should drop by 1: 3-1=2", 2, manaCake.getSellIn());
		assertEquals(7, manaCake.getQuality());

	}
	
	@Test
	public void testUpdatebyEndOfTheWeek_ConjuredManaCake_5daysAfterSellIn() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Conjured Mana Cake", 5, 14) );
		
		// Act
		for (int i = 0; i < 7; i++) {
			store.updateEndOfDay();
		}
		
		// Assert
		List<Item> items = store.getItems();
		Item manaCake = items.get(0);
		assertEquals("Conjured Mana Cake", manaCake.getName());
		assertEquals("Conjured Mana cakes quality should drop twice as fast after sell in 14-5-2*2=5",
				5,
				manaCake.getQuality());
		assertEquals(-2, manaCake.getSellIn());

	}
	
	@Test
	public void testUpdateAfterMonth_sulfurasQualityDoesNotDecrease() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80) );
		
		// Act
		for (int i = 0; i < 30; i++) {
			store.updateEndOfDay();
		}
		
		// Assert
		List<Item> items = store.getItems();
		Item sulfuras = items.get(0);
		assertEquals("Sulfuras quality should be same 80=80",
				80,
				sulfuras.getQuality());
		assertEquals(0, sulfuras.getSellIn());

	}
}
