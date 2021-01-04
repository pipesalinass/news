/*
 * Copyright 2020 Felipe Salinas-Urra, piipebysonic@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cl.ucn.disc.dsm.fsalinas.news.services;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.kwabenaberko.newsapilib.models.Article;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import cl.ucn.disc.dsm.fsalinas.news.model.News;
import cl.ucn.disc.dsm.fsalinas.news.utils.Validation;

/**
 * The NewsApi implementation of Contracts.
 *
 * @author Felipe Salinas-Urra.
 */
public class ContractImplNewsApi implements Contracts {

    /**
     *  The Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(ContractImplNewsApi.class);

    /**
     *  The connection to NewsApi.
     */
    private final NewsApiService newsApiService;

    /**
     * The Constructor.
     *
     * @param apiKey to use.
     */
    public ContractImplNewsApi(String apiKey) {

        Validation.minSize(apiKey,10, "Apikey!");

        this.newsApiService = new NewsApiService (apiKey);
    }

    /**
     * Filter the Stream.
     *
     * @param idExtractor
     * @param <T> news to filter.
     * @return true if the news already exists.
     */
    private static <T> Predicate<T> distintById(Function<? super T, ?> idExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(idExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Get the list of News.
     *
     * @param size size of the list.
     * @return the List of News.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<News> retrieveNews(Integer size) {
        //TODO OJO DESPUES ARREGLAR TOP HEADLINES, try catch.
        try {
            // Request to NewsApi
            List<Article> articles = this.newsApiService.getTopHeadlines("general", size);

            //  The final list of News.
            List<News> news = new ArrayList<>();

            // Iterate over the articles.
            for(Article article: articles) {
                // Article -> News
                news.add(toNews(article));
            }

            // .. return the list of News.
            return news.stream()
                    // Remove the duplicates by id
                    .filter(distintById(News::getId))
                    // Sort the stream by publishedAt
                    .sorted((k1, k2) -> k2.getPublishedAt().compareTo(k1.getPublishedAt()))
                    // Return the stream to list
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Error", e);
            // Inner exception
            throw new RuntimeException(e);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Article to News. (Transformer Pattern).
     *
     * @param article to  convert.
     * @return the News.
     */
    private static News toNews(final Article article) {

        log.debug("Article: {}. ", ToStringBuilder.reflectionToString(
                article, ToStringStyle.MULTI_LINE_STYLE
        ));

        // The date
        ZonedDateTime publishedAt = ZonedDateTime
                .parse(article.getPublishedAt())
                .withZoneSameInstant(ZoneId.of("-3"));

        Validation.notNull(article, "Article null");

        // Warning message?
        boolean needFix = false;

        // Fixing the restrictions.
        if (article.getAuthor() == null || article.getAuthor().length() == 0) {
            article.setAuthor("No Author*");
            needFix = true;
        }

        // Fixing more restrictions
        if (article.getDescription() == null || article.getDescription().length() == 0) {
            article.setDescription("No Description*");
            needFix = true;
        }

        // .. yes, warning message.
        if (needFix) {
            // Debug of Article
            log.warn("Article with invalid restrictions: {}." , ToStringBuilder.reflectionToString(article, ToStringStyle.MULTI_LINE_STYLE));
        }

        return new News(
                article.getTitle(),
                article.getSource().getName(),
                article.getAuthor(),
                article.getUrl(),
                article.getUrlToImage(),
                article.getDescription(),
                article.getDescription(), // FIXME: WHERE IS THE CONTENT?
                publishedAt


        );
    }

    /**
     * Save one News into the System.
     *
     * @param news to save.
     */
    @Override
    public void saveNews(News news) {
        throw new NotImplementedException("Can't save news on NewsAPI");
    }
}
