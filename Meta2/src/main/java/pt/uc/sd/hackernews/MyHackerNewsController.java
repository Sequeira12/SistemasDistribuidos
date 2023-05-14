package pt.uc.sd.hackernews;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import pt.uc.sd.forms.Client;
import pt.uc.sd.meta1files.MessageServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MyHackerNewsController {
    private static final Logger log = LoggerFactory.getLogger(MyHackerNewsController.class);

    @GetMapping("/hackernews")
    private ModelAndView hackerNewsTopStories(@RequestParam(name = "search", required = false) String search) {
        System.out.println(search);
        String topStoriesEndpoint = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
        int conta = 0;
        RestTemplate restTemplate = new RestTemplate();
        List hackerNewsNewTopStories = restTemplate.getForObject(topStoriesEndpoint, List.class);

        assert hackerNewsNewTopStories != null;
        log.info("hackerNewsNewStories: " + hackerNewsNewTopStories);
        log.info("hackerNewsNewStories: " + hackerNewsNewTopStories.size()); // Up to 500 top stories
        try {
            MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
            List<HackerNewsItemRecord> hackerNewsItemRecordList = new ArrayList<>();
            for (int i = 0; i < hackerNewsNewTopStories.size(); i++) {
                Integer storyId = (Integer) hackerNewsNewTopStories.get(i);

                String storyItemDetailsEndpoint = String.format("https://hacker-news.firebaseio.com/v0/item/%s.json?print=pretty", storyId);
                HackerNewsItemRecord hackerNewsItemRecord = restTemplate.getForObject(storyItemDetailsEndpoint, HackerNewsItemRecord.class);
                if (conta == 10) break;

                if (hackerNewsItemRecord == null) {
                    //log.error("Item " + storyId + " is null");
                    continue;
                }
                if (hackerNewsItemRecord.url() == null || hackerNewsItemRecord.text() == null) {
                    continue;
                }


                //log.info("hackerNewsNewStories (details of " + storyId + "): " + hackerNewsItemRecord);

                if (search != null) {
                    //log.info("search: " + search);
                    List<String> searchTermsList = List.of(search.toLowerCase().split(" "));
                    if (searchTermsList.stream().anyMatch(hackerNewsItemRecord.text()::contains)) {
                        hackerNewsItemRecordList.add(hackerNewsItemRecord);
                        conta++;
                        h.SendUrltoQueue(hackerNewsItemRecord.url());
                    }
                } else {
                    //System.out.println("No search terms");
                    //hackerNewsItemRecordList.add(hackerNewsItemRecord);
                }
            }
            if(hackerNewsItemRecordList.size()==0){
                System.out.println("nenhuma story contem esses tokens");
            }

            for(int i = 0; i<hackerNewsItemRecordList.size(); i++){
                System.out.println(hackerNewsItemRecordList.get(i).title() + " " + hackerNewsItemRecordList.get(i).url());
                System.out.println(hackerNewsItemRecordList.get(i).text());
            }

        } catch (RemoteException | NotBoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        String toReturn = "redirect:/search?show=true&page=1&token=";
        String newtoken=search.replace("%20", " ");
        toReturn += newtoken;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(toReturn);
        return modelAndView;
    }

    @GetMapping("/hackernewsindex")
    public ModelAndView newHackerIndexing(Model model) {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);


        Client client = (Client) session.getAttribute("cliente");
        String nome;
        if (client == null || client.getUsername()==null) nome = "Not logged in";
        else nome = client.getUsername();
        model.addAttribute("username", nome);
        model.addAttribute("logado", session.getAttribute("logado"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hackerNewsIndexing");
        return modelAndView;
    }

    @PostMapping("/hackernewsindex")
    private ModelAndView hackerNewsTopStories(@ModelAttribute Client client) {
        try {
            String username = client.getUsername();
            System.out.println(username);
            MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
            RestTemplate restTemplate = new RestTemplate();
            String hackernewsuser = String.format("https://hacker-news.firebaseio.com/v0/user/%s.json?print=pretty", username);
            HackerNewsUserRecord hackerNewsUser = restTemplate.getForObject(hackernewsuser, HackerNewsUserRecord.class);
            List lista = hackerNewsUser.submitted();
            System.out.println(lista);
            for(int i = 0; i<lista.size(); i++){
                Integer storyId = (Integer) lista.get(i);

                String storyItemDetailsEndpoint = String.format("https://hacker-news.firebaseio.com/v0/item/%s.json?print=pretty", storyId);
                HackerNewsItemRecord hackerNewsItemRecord = restTemplate.getForObject(storyItemDetailsEndpoint, HackerNewsItemRecord.class);
                if (hackerNewsItemRecord == null || hackerNewsItemRecord.url() == null) {
                    //log.error("Item " + storyId + " is null");
                    continue;
                }
                else{
                    System.out.println(lista.get(i));
                    System.out.println(hackerNewsItemRecord.url());
                    h.SendUrltoQueue(hackerNewsItemRecord.url());
                }
            }

        } catch (RemoteException | NotBoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/search");
        return modelAndView;
    }
}