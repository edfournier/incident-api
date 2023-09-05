package com.group.api.jobs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group.api.repository.IncidentRepository;

@Component
public class Scraper {

    private IncidentRepository repository;

    public Scraper(IncidentRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 11,23 * * *")
    public void scrapeToday() {
        scrapeBack(0);
    }
    
    //@Scheduled(fixedDelay = 600000)
    @Scheduled(cron = "0 0 11 * * WED")
    public void scrapeWeek() {
        scrapeBack(7);
    }

    /*
     * Scrapes all days in [today, today - n]. 
     */
    private void scrapeBack(int n) {
        try {
            LocalDateTime today = LocalDateTime.now();

            for (int i = 0; i <= n; i++) {
                // Scrape html posted i days before today. 
                LocalDateTime sub = today.minus(i, ChronoUnit.DAYS);
                String date = sub.format(DateTimeFormatter.ofPattern("EEEE-MMMM-d-yyyy")).toLowerCase();
                String key = sub.format(DateTimeFormatter.ofPattern("M-d-yyyy")).toLowerCase();

                // Verify incident is not already in database.
                if (repository.existsIncident(key)) {
                    continue;
                }
                
                Document html = Jsoup.connect("https://www.umass.edu/umpd/daily-crime-log/" + date + "-crime-log")
                    .ignoreHttpErrors(true)
                    .get();

                // Check html is an incident log. 
                Element title = html.select("h1#page-title").first();
                if (title.text().equals("Page Not Found")) {
                    continue;
                }

                // Parse incidents from log, and store in database.
                Elements incidents = html.select("div.field-item.even").first().children();
                String[] log = new String[incidents.size()];
                for (int j = 0; j < incidents.size(); j++) {
                    log[j] = incidents.get(j).text();
                }
                repository.addIncident(key, log);
            }
            System.out.println("Finished scraping the past " + n + " days");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
