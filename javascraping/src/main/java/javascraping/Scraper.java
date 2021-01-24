package javascraping;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	public static void scrapeAmazonData(String url) throws IOException, InterruptedException {
		Document doc = Jsoup.connect(url).get();

		// Get the list of items
		Elements items = doc.getElementsByClass("s-result-item");

		if (items.size() == 0) {
			return;
		}

		System.out.println("Scarping...\n" + url);

		// Get all items on current page
		for (Element item : items) {

			// Get image url
			String imageUrl = item.getElementsByClass("s-image").attr("src");

			// Get name of the product
			String name = item.getElementsByAttributeValueContaining("class", "a-color-base a-text-normal").text();

			// Get price of the product
			String price = item.getElementsByClass("a-offscreen").isEmpty() ? ""
					: item.getElementsByClass("a-offscreen").text();

			// Get product URL
			String productUrl = "https://www.amazon.com/"
					+ item.getElementsByAttributeValueContaining("class", "a-link-normal a-text-normal").attr("href");

			if (!name.isEmpty()) {
				storeInFile(productUrl, imageUrl, name, price, "", "");
			}

			// Sleep a while to give rest to servers
			Thread.sleep(400 + new Random().nextInt(200));
		}
	}

	public static void scrapeEbayData(String url, String category) throws IOException, InterruptedException {
		// Ebay example url https://www.ebay.com/b/Bedding/20444/bn_1864382

		// Fetch webpage
		Document doc = Jsoup.connect(url).get();

		// Get list of items
		Elements items = doc.getElementsByClass("s-item");

		if (items.size() == 0) {
			return;
		}

		System.out.println("Scarping...\n" + url);

		// Traverse each item
		for (Element item : items) {
			// Get image url
			String imageUrl = item.getElementsByClass("s-item__image-img").attr("src");

			// Get product url
			String productUrl = item.getElementsByClass("s-item__link").attr("href");

			// fetch product page
			Document productDoc = Jsoup.connect(productUrl).get();
			
			String model = "";

			if(productDoc.getElementsByAttributeValueContaining("itemprop", "model").size() != 0) {
				model = productDoc.getElementsByAttributeValueContaining("itemprop", "model").text();
			} else if(productDoc.getElementsByClass("spec-row").size() != 0) {
				// Get all spec-rows divs
				Elements rows = productDoc.getElementsByClass("spec-row");
				// Get spec items from spec-rows
				for(Element row: rows) {
					for(Element specItem: row.getElementsByClass("s-name")) {
						// Check if item is Model
						if(specItem.text().equals("Model")) {
							model = specItem.nextElementSibling().text();
							break;
						}
					}					
				}
			}

			// Get product name
			String name = item.getElementsByClass("s-item__title").first().children().size() == 0
					? item.getElementsByClass("s-item__title").text()
					: item.getElementsByClass("s-item__title").first().child(0).text();

			// Get product price
			String price = item.getElementsByClass("s-item__price").text();

			// Give rest to ebay servers
			Thread.sleep(250);
			if (!name.isEmpty()) {
				storeInFile(productUrl, imageUrl, name, price, model, category);
			}
			// Sleep a while to give rest to servers
			Thread.sleep(400 + new Random().nextInt(200));
		}
	}

	public static void scrapeNewEgg(String url, String category) throws IOException, InterruptedException {
		Document doc = Jsoup.connect(url).get();

		// Get list of items
		Elements items = doc.getElementsByClass("item-cell");

		if (items.size() == 0) {
			return;
		}

		System.out.println("Scarping...\n" + url);

		// Traverse each item
		for (Element item : items) {
			// Get product url
			String productUrl = item.getElementsByClass("item-img").attr("href");

			// Fetch product page
			Document productDoc = Jsoup.connect(productUrl).get();
			// Get table of specifications
			Element specTable = productDoc.getElementsByClass("table-horizontal").first().child(1);
			String model = "";
			if(specTable.childNodeSize() > 0) {
				// Traverse the table and find the model of product
				for(Element tableRow: specTable.children()) {
					if(tableRow.child(0).text().equals("Series") 
					|| tableRow.child(0).text().equals("\"Series\"")) {
						model = tableRow.child(1).text();
						break;
					}
				}
			}

			// Get image url
			String imageUrl = item.getElementsByClass("item-img").first().child(0).attr("src");

			// Get product name
			String name = item.getElementsByClass("item-title").text();

			// Get product price
			String price = "$" + item.getElementsByClass("price-current").first().getElementsByTag("strong").text()
					+ item.getElementsByClass("price-current").first().getElementsByTag("sup").text();

			if (!name.isEmpty()) {
				storeInFile(productUrl, imageUrl, name, price, model, category);
			}

			// Give rest to servers
			Thread.sleep(250);
		}
	}

	public static void scrapeKelkoo(String url) throws IOException, InterruptedException {
		// Fetch webpage
		Document doc = Jsoup.connect(url).get();

		// Get list of elements
		Elements items = doc.getElementsByClass("offer-grid__item");

		if (items.size() == 0) {
			return;
		}

		System.out.println("Scarping...\n" + url);

		for (Element item : items) {
			// Get product name
			String name = item.getElementsByClass("offer-box__title").text();

			// Get image url
			String imageUrl = item.getElementsByClass("offer-image__link-go").first().child(0).attr("data-src");

			// Get product Url
			String productUrl = item.getElementsByClass("offer-box__content-container--link").attr("href");

			// Get price
			String price = item.getElementsByClass("offer-price__total").first().text()
					+ item.getElementsByClass("price-fraction").first().text();
			// Replace "," with ""
			price = price.substring(1).replace(",", "");
			// Convert price to dollar
			price = "$" + (Float.parseFloat(price) * 1.33);
			if (!name.isEmpty()) {
				// We aren't finding model and brand (category) of the products on kelkoo because
				// kelkoo collects its products from several websites so 
				// we decided to not devise a way to scrape every website and leave the model and brand(category) empty
				storeInFile(productUrl, imageUrl, name, price, "", "");
			}
			Thread.sleep(250);
		}
	}

	public static void scrapeSwappa(String url, String category) throws IOException, InterruptedException {

		// Fetch webpage
		Document doc = Jsoup.connect(url).get();

		// Get list items
		Elements items = doc.getElementsByAttributeValueContaining("class", "col-xs-4 col-sm-3 col-md-2 col-lg-2");

		if (items.size() == 0) {
			return;
		}

		System.out.println("Scarping...\n" + url);

		for (Element item : items) {
			// Get product URL
			String productUrl = item.getElementsByClass("image").attr("abs:href");

			// Get image url
			String imageUrl = item.getElementsByClass("image").first().child(0).attr("src");

			// Get product name
			String name = item.getElementsByClass("title").text();

			// Get product price
			String price = "$" + item.getElementsByClass("price").first().getElementsByTag("span").text();

			if (!name.isEmpty()) {
				// Swappa does not provide the specifications of a phone
				// so we left out the model of the phone.
				storeInFile(productUrl, imageUrl, name, price, "", category);
			}
			Thread.sleep(250);
		}
	}

	private static void storeInFile(String productUrl, String imageUrl, String name, String price, String model, String category) throws IOException {
		// Open file
		FileWriter dataFileWriter = new FileWriter("data.txt", true);
		// Write data to file
		dataFileWriter.write(productUrl + "\n");
		dataFileWriter.write(imageUrl + "\n");
		dataFileWriter.write(name + "\n");
		dataFileWriter.write(price + "\n");
		dataFileWriter.write(model + "\n");
		dataFileWriter.write(category + "\n\n");

		// Close write
		dataFileWriter.close();
	}
}
