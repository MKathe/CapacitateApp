package com.cerezaconsulting.compendio.utils;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.cerezaconsulting.compendio.data.entities.LocalUrls;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {


    public static ArrayList<LocalUrls> showLinks(String text, Context context, String id) {
        ArrayList<LocalUrls> strings = new ArrayList<>();


        Document doc = Jsoup.parse(text);
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia--: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));

                Log.e("IMAGEEEE", src.attr("abs:src"));
                strings.add(new LocalUrls(src.attr("abs:src"), id));


            } else{
                print("*** %s: <%s>", src.tagName(), src.attr("abs:src"));
            }

        }

        print("\nImports-a: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks-b: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }

        return strings;
    }


    public static boolean ifContentInternetData(ArrayList<FragmentEntity> fragmentEntities) {

        int count = 0;
        for (int i = 0; i < fragmentEntities.size(); i++) {
            Document doc = Jsoup.parse(fragmentEntities.get(i).getContent());
            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");

            print("\nMedia: (%d)", media.size());
            for (Element src : media) {
                if (src.tagName().equals("img")) {
                    print(" * %s: <%s> %sx%s (%s)",
                            src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                            trim(src.attr("alt"), 20));

                    Log.e("IMAGEEEE", src.attr("abs:src"));


                } else {
                    count++;
                    print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
                }

            }

            print("\nImports a: (%d)", imports.size());
            for (Element link : imports) {
                print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
            }

            print("\nLinks b: (%d)", links.size());
            for (Element link : links) {
                print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
            }
        }


        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }


    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = args[0];
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
