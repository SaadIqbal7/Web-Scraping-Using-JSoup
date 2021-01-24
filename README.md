# Web Scraping Using JSoup

This repository contains scripts to scrape mobile products from 5 different websites using Java's scraping library, JSoup.

The 5 websites that were scraped are:
- [Amazon](https://www.amazon.com/)
- [Ebay](https://www.ebay.com/)
- [Swappa](https://swappa.com/)
- [New Egg](https://www.newegg.com/)
- [Kelkoo](https://www.kelkoo.co.uk/)

The scraped links are either provided in the links file or they are hardcoded in the code. When you run the code, the scraped data will be stored in data.txt file.

In order to scrape websites faster, thread were used using a fixed thread pool.

Note: Amazon might block you from scraping so use libraries such as Selenium to scrape Amazon.
