package com.book.clue.kotbook;

import com.book.clue.kotbook.util.Network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import okhttp3.ResponseBody;

public class jte {

   @Test
   public void test() {
      Network network = new Network();
      network.getGBookApi()
             .getFromUrl("http://gravitytales.com/")
             .map(ResponseBody::string)
             .map(Jsoup::parse)
             .subscribe(this::abc);
   }

   public void abc(Document document) {
      System.out.println();
   }
}
