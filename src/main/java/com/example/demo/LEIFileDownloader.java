//package com.example.demo;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.HelpFormatter;
//import org.apache.commons.cli.Options;
//import org.apache.commons.configuration.ConfigurationException;
//import org.apache.commons.configuration.PropertiesConfiguration;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
//
//import net.bedata.atlas.lei.downloader.exceptions.LEIFileDownloaderException;
//import net.bedata.atlas.lei.downloader.utils.HtmlUtils;
//
//public class LEIFileDownloader {
//
//    private static final Logger logger = LoggerFactory.getLogger(LEIFileDownloader.class);
//    private PropertiesConfiguration configuration;
//
//    public LEIFileDownloader(String propertiesFile) throws ConfigurationException {
//        // Get configuration from properties files
//        configuration = new PropertiesConfiguration(propertiesFile);
//        configuration.setThrowExceptionOnMissing(true); // throw an exception if a property is missing
//    }
//
//    public void downloadLeiFile() throws LEIFileDownloaderException {
//        try {
//            // Step 1 : download the page containing the zipped file link
//            logger.info("Step1 : download the content of the html page = {}",configuration.getString("lei.url"));
//            String htmlContent = HtmlUtils.getHtmlContent(configuration.getString("lei.url"));
//            logger.debug("==================== HtmlContent : {}",htmlContent);
//            // Step 2 : isolate the url that will be used to download the zipped file
//            logger.info("Step 2 : isolate the url that will be used to download the zipped file");
//            String fullZippedUrlFile = getFullZippedUrlFile(htmlContent,computePattern(configuration.getString("lei.fullFile.pattern")));
//            logger.info("zipped file link = {}",fullZippedUrlFile);
//            // Step 3 : download the zipped file
//            logger.info("Step 3 : download file to {}",configuration.getString("lei.fullFile.destination"));
//            HtmlUtils.downloadFile(fullZippedUrlFile, new File(configuration.getString("lei.fullFile.destination")));
//        } catch (FailingHttpStatusCodeException | IOException e) {
//            logger.error("Error in downloadLeiFile method", e);
//            throw new LEIFileDownloaderException(e);
//        }
//    }
//
//    public String getFullZippedUrlFile(String htmlContent, String pattern) {
//        Document document = Jsoup.parse(htmlContent); // parse html document
//        Elements hrefElements = document.getElementsByTag("a"); // keep only "a" tag
//
//        List<Element> collect = hrefElements.stream().filter(element -> element.attr("href").matches(pattern))
//                .collect(Collectors.toList());
//        // Keep only the first one (the most recent)
//        return collect.get(0).attr("href");
//    }
//
//    private String computePattern(String pattern) {
//        final LocalDate today = LocalDate.now();
//        final DecimalFormat mFormat = new DecimalFormat("00");
//
//        final String finalPattern = pattern.replaceAll("<year>", "" + today.getYear())
//                .replaceAll("<month>", "" + mFormat.format(today.getMonthValue()))
//                .replaceAll("<day>", "" + mFormat.format(today.getDayOfMonth()));
//        return finalPattern;
//    }
//
//    public static void main(String[] args) {
//        // List of command line options
//        Options options = new Options();
//        options.addOption("pf", "propertiesFile", true, "Properties File");
//        LEIFileDownloader leiFileDownloader;
//        try {
//            // Args to options
//            CommandLine cmd = new DefaultParser().parse(options, args);
//            if (cmd.hasOption("pf")) {
//                leiFileDownloader = new LEIFileDownloader(cmd.getOptionValue("pf"));
//                leiFileDownloader.downloadLeiFile();
//            } else {
//                throw new IllegalArgumentException(); // see catch
//            }
//        } catch (IllegalArgumentException e) {
//            logger.error("Syntax error !");
//            new HelpFormatter().printHelp("LegalEntityIdentifierLoader", options);
//            System.exit(101);
//        } catch (LEIFileDownloaderException e) {
//            logger.error("LEIFileDownloaderException", e);
//            System.exit(102);
//        } catch (Exception e) {
//            logger.error("Error", e);
//            System.exit(199);
//
//        }
//    }
//
//}
