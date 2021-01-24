package javascraping;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
	public static void main(String[] args) throws IOException, InterruptedException {

		// Define executor service with n number of threads.
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		String amazonLink;
		String ebayLink;
		String swappaLink;
		String kelkooLink;
		String neweggLink;

		String category;
		String link;
		int pages;

		// Scrape m pages of amazon mobile page link
		for (int i = 1; i <= 50; i++) {
			amazonLink = "https://www.amazon.com/s?i=mobile&rh=n%3A2335752011%2Cn%3A2335753011%2Cn%3A7072561011"
					+ "&page=" + i + "&ref=sr_pg_" + i;
			executorService.execute(new UserThread(new ThreadInterface() {
				@Override
				public void func(String... args) {
					try {
						Scraper.scrapeAmazonData(args[0]);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, amazonLink));
		}
		executorService.shutdown();

		// Scrape ebay data
		Scanner ebayLinksFileReader = new Scanner(new File("ebay links.txt"));
		while (ebayLinksFileReader.hasNextLine()) {
			// Get brand (category) from file
			category = ebayLinksFileReader.nextLine();
			// Get link from file
			link = ebayLinksFileReader.nextLine();

			// Read blank space
			ebayLinksFileReader.nextLine();

			// Scrape m pages of ebay mobile page link
			for (int i = 1; i <= 50; i++) {
				ebayLink = link + "?rt=nc&_pgn=" + i;
				executorService.execute(new UserThread(new ThreadInterface() {
					@Override
					public void func(String... args) {
						try {
							Scraper.scrapeEbayData(args[0], args[1]);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, ebayLink, category));
			}
		}
		ebayLinksFileReader.close();
		executorService.shutdown();

		// Scrape newegg data
		Scanner newEggFileReader = new Scanner(new File("newegg links.txt"));
		while (newEggFileReader.hasNextLine()) {
			// Read brand(category) from file
			category = newEggFileReader.nextLine();
			// Read link from file
			link = newEggFileReader.nextLine();
			// Read blank space
			newEggFileReader.nextLine();

			// Scrape m pages of each category of phone on newEgg
			for (int i = 1; i <= 50; i++) {
				neweggLink = link + "&page=" + i;
				executorService.execute(new UserThread(new ThreadInterface() {
					@Override
					public void func(String... args) {
						try {
							Scraper.scrapeNewEgg(args[0], args[1]);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, neweggLink, category));
			}
		}
		executorService.shutdown();
		newEggFileReader.close();

		// Scrape m pages kelkoo mobile page
		for (int i = 1; i <= 16; i++) {
			kelkooLink = "https://www.kelkoo.co.uk/c/mobiles-phones-faxes/mobile-phones/sim-free-mobile-phones-100020213/"
					+ "?page=" + i;
			executorService.execute(new UserThread(new ThreadInterface() {
				@Override
				public void func(String... args) {
					try {
						Scraper.scrapeKelkoo(args[0]);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, kelkooLink));
		}
		executorService.shutdown();

		// Scrape swappa data
		Scanner swappaFileReader = new Scanner(new File("swappa links.txt"));
		while (swappaFileReader.hasNextLine()) {
			// Read brand (category) from file
			category = swappaFileReader.nextLine();
			// Read link from file
			link = swappaFileReader.nextLine();
			// When a page is not available on swappa, rather than
			// redirecting to an error page, it sends the user to
			// the first page of a specific type of phone (like first page
			// of samsung phones)
			// So we keep track of the number of pages in the file
			// So get the number of pages
			pages = Integer.parseInt(swappaFileReader.nextLine());

			// Read blank space
			swappaFileReader.nextLine();
			// Scrape m pages of each category of phone on swappa
			for (int i = 1; i <= pages; i++) {
				swappaLink = link + "?page=" + i;
				executorService.execute(new UserThread(new ThreadInterface() {
					@Override
					public void func(String... args) {
						try {
							Scraper.scrapeSwappa(args[0], args[1]);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, swappaLink, category));
			}
		}
		executorService.shutdown();
		swappaFileReader.close();
	}
}
