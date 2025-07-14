package org.example

// Использую htmlunit из-за динамического контента на сайте
// Jsoup работает со статикой, на сайте у нас React
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlPage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

const val URL_QUOTES =
    "http://mybook.ru/author/duglas-adams/avtostopom-po-galaktike-restoran-u-konca-vselennoj/citations/"
const val SELECTOR_QUOTES = "article"
const val TIMEOUT_LOADING_JS: Long = 10000

fun main() {
    // webClient - эмулятор браузера, для загрузки динамического контента
    val webClient = WebClient().apply {
        options.apply {
            isJavaScriptEnabled = true // включаем js
            isCssEnabled = false // отключаем css стили
            isThrowExceptionOnScriptError = false  // Игнорировать JS ошибки
        }
        // Ожидание фоновых JS-запросов
        waitForBackgroundJavaScript(TIMEOUT_LOADING_JS)
    }

    // Запрашиваю динамическую страницу по URL
    val page: HtmlPage = webClient.getPage(URL_QUOTES)
    // Ожидаю загрузку JS
    webClient.waitForBackgroundJavaScript(TIMEOUT_LOADING_JS)

    // Преобразуем текущую веб-страницу в XML-строку
    val pageXml = page.asXml()

    // Обрабатываю полученную статику
    val doc: Document = Jsoup.parse(pageXml);
    val articles: Elements = doc.select(SELECTOR_QUOTES)

    // Вывожу цитаты
    for (article: Element in articles) {
        println(article.text())
    }
}
