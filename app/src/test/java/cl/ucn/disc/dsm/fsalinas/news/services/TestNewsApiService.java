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

import com.kwabenaberko.newsapilib.models.Article;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Testing the NewsApiService
 * @author Felipe Salinas-Urra.
 */
public class TestNewsApiService {

    /**
     * The Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(TestNewsApiService.class);

    /**
     * The Testing.
     */
    public void wrongApi() throws IOException {

        log.debug("Testing ..");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NewsApiService newsApiService = new NewsApiService(null);
        });

        log.debug("Wrong Key ..");

        Assertions.assertThrows(RuntimeException.class, () -> {
            NewsApiService newsApiService = new NewsApiService("This is the wrong key");
            List<Article> articles = newsApiService.getTopHeadlines("general", 10);
            log.debug("Articles: {}. ", articles);
        });

        log.debug("Good Key ..");
        {
            // TODO: Add the real Apikey
            NewsApiService newsApiService = new NewsApiService("<USE REAL APIKEY>");
            List<Article> articles = newsApiService.getTopHeadlines("general", 10);
            log.debug("Articles: {}. ", articles);
            log.debug("Articles size: {}. ", articles.size());
        }

        {
            // TODO: Add more testing to the backend.
        }

        log.debug(".. Done.");
    }

}
