package javaProject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SchoolWebCrawling {

    public static void main(String[] args) {
        System.out.println("(1)알림마당 -> 보도자료");
        System.out.println("(2)알림마당 -> 대외수상");
        System.out.println("(3)알림마당 -> 채용공고");
        System.out.print("조회할 항목을 선택하세요: ");
        int option = new Scanner(System.in).nextInt();

        String baseUrl = "";
        String category = "";

        switch (option) {
            case 1:
                baseUrl = "http://school.gyo6.net/gbsw/na/ntt/selectNttList.do?mi=122589&bbsId=203245";
                category = "보도자료";
                break;
            case 2:
                baseUrl = "http://school.gyo6.net/gbsw/na/ntt/selectNttList.do?mi=122588&bbsId=210793";
                category = "대외수상";
                break;
            case 3:
                baseUrl = "http://school.gyo6.net/gbsw/na/ntt/selectNttList.do?mi=122599&bbsId=221645";
                category = "채용공고";
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                return;
        }

        Connection conn = Jsoup.connect(baseUrl);

        try {
            Document document = conn.get();

            Element htmlElement = document.selectFirst("div.box_info_wrap");
            String pureText = htmlElement.text();
            System.out.println(pureText);

            System.out.print("페이지를 몇 개 출력할지 선택하세요(최대 " + getMaxPage(option) + "페이지): ");
            int numPages = new Scanner(System.in).nextInt();

            List<String> urls = generateUrls(baseUrl, numPages);

            int page = 1;

            for (String url : urls) {
                conn = Jsoup.connect(url);
                document = conn.get();

                Elements rows = document.select("tbody > tr");
                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.size() > 0) {
                        String postNumber = cols.get(0).text();
                        if (postNumber.matches("\\d+")) {
                            String title = cols.get(1).text();
                            String author = cols.get(2).text();
                            String viewCount = cols.get(3).text();
                            String date = cols.get(4).text();

                            System.out.println("게시물 번호 " + postNumber);
                            System.out.println("게시물 제목 " + title);
                            System.out.println(author);
                            System.out.println(date);
                            System.out.println(viewCount);
                            System.out.println("----------------------------------------------------------------------");
                        }
                    }
                }

                System.out.println("현재 " + category + " 페이지 번호: " + page);
                System.out.println("현재 " + category + " 페이지 URI: " + url);
                System.out.println("======================================================================");

                page++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> generateUrls(String baseUrl, int numPages) {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            String url = baseUrl + "&page=" + i;
            urls.add(url);
        }
        return urls;
    }

    private static int getMaxPage(int option) {
        switch (option) {
            case 1:
                return 11;
            case 2:
                return 3;
            case 3:
                return 2;
            default:
                return 0;
        }
    }
}
