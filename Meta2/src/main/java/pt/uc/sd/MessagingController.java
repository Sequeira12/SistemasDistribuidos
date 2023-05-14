package pt.uc.sd;

import jakarta.servlet.http.HttpSession;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;
import pt.uc.sd.forms.Client;
import pt.uc.sd.forms.TokensParaPesquisa;
import pt.uc.sd.forms.UrlsForQueue;
import pt.uc.sd.meta1files.IBarrelRemoteInterface;
import pt.uc.sd.meta1files.InterfaceDownloaders;
import pt.uc.sd.meta1files.MessageServerInterface;
import pt.uc.sd.models.Resultado;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Controller
public class MessagingController {

    @RequestMapping("/stats")
    public String start(Model model) {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);



        Client client = (Client) session.getAttribute("cliente");
        String nome;
        if (client == null || client.getUsername()==null) nome = "Not logged in";
        else nome = client.getUsername();
        System.out.println(session.getAttribute("logado") + "AHHAHAH");
        model.addAttribute("username", nome);
        model.addAttribute("logado", session.getAttribute("logado"));
        return "status";
    }


    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public CompletableFuture<Message> onMessage(Message message) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
                if ("first".equals(message.content())) {
                    ArrayList<InterfaceDownloaders> downloaders = h.getDownloaders();

                    ArrayList<IBarrelRemoteInterface> barrels = h.getBarrels();

                    StringBuilder Final = new StringBuilder();

                    for (int i = 0; i < downloaders.size(); i++) {
                        String endpoint = RemoteObject.toStub(downloaders.get(i)).toString();
                        // Extrai o IP e a porta do endpoint
                        String[] endpointParts = endpoint.split(":");

                        String ip = endpointParts[2].substring(1);
                        String porta = endpointParts[3].substring(0, endpointParts[3].lastIndexOf("]"));

                        // Exibe o IP e a porta do endpoint
                        String infoB = (i + 1) + " - IP: " + ip + " Porta: " + porta + ",";
                        Final.append(infoB);
                    }


                    Final.append("||");

                    for (int i = 0; i < barrels.size(); i++) {
                        String endpoint = RemoteObject.toStub(barrels.get(i)).toString();
                        // Extrai o IP e a porta do endpoint
                        String[] endpointParts = endpoint.split(":");

                        String ip = endpointParts[2].substring(1);
                        String porta = endpointParts[3].substring(0, endpointParts[3].lastIndexOf("]"));

                        // Exibe o IP e a porta do endpoint
                        String infoB = (i + 1) + " - IP: " + ip + " Porta: " + porta + ",";
                        Final.append(infoB);


                    }
                    String top10 = h.VerificaTop10();
                    Final.append("||");
                    Final.append(top10);
                    String f = Final.toString();
                    h.setUpdated(false);
                    return new Message(HtmlUtils.htmlEscape(f));
                } else {
                    if (h.getUpdated()==true) {
                        System.out.println(h.getUpdated());
                        ArrayList<InterfaceDownloaders> downloaders = h.getDownloaders();
                        ArrayList<IBarrelRemoteInterface> barrels = h.getBarrels();

                        StringBuilder Final = new StringBuilder();

                        for (int i = 0; i < downloaders.size(); i++) {
                            String endpoint = RemoteObject.toStub(downloaders.get(i)).toString();
                            // Extrai o IP e a porta do endpoint
                            String[] endpointParts = endpoint.split(":");

                            String ip = endpointParts[2].substring(1);
                            String porta = endpointParts[3].substring(0, endpointParts[3].lastIndexOf("]"));

                            // Exibe o IP e a porta do endpoint
                            String infoB = (i + 1) + " - IP: " + ip + " Porta: " + porta + ",";
                            Final.append(infoB);
                        }


                        Final.append("||");

                        for (int i = 0; i < barrels.size(); i++) {
                            String endpoint = RemoteObject.toStub(barrels.get(i)).toString();
                            // Extrai o IP e a porta do endpoint
                            String[] endpointParts = endpoint.split(":");

                            String ip = endpointParts[2].substring(1);
                            String porta = endpointParts[3].substring(0, endpointParts[3].lastIndexOf("]"));

                            // Exibe o IP e a porta do endpoint
                            String infoB = (i + 1) + " - IP: " + ip + " Porta: " + porta + ",";
                            Final.append(infoB);


                        }
                        String top10 = h.VerificaTop10();
                        Final.append("||");
                        Final.append(top10);
                        String f = Final.toString();
                        h.setUpdated(false);
                        return new Message(HtmlUtils.htmlEscape(f));
                    }
                }
                return new Message(HtmlUtils.htmlEscape(""));
            } catch (RemoteException | NotBoundException | SQLException e) {
                throw new RuntimeException(e);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
/*
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message onMessage(Message message) throws InterruptedException {
        Thread.sleep(1000);
        return new Message(HtmlUtils.htmlEscape(message.content()));
    }*/



    @GetMapping("/")
    public String redirect() {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("logado", false);
        return "redirect:/search";
    }


    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    @PostMapping("/login")
    public String getLogin(@ModelAttribute Client client, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        try {
       

            MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
            boolean noError = h.Login(client.getUsername(), client.getPassword());
            if (noError) {
                session.setAttribute("logado", true);
                session.setAttribute("cliente", client);
                System.out.println(client.getUsername());
                System.out.println("logado");
            } else {
                System.out.println("erro no login");
                model.addAttribute("error", true);
                return "login";
            }
        } catch (RemoteException | NotBoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/search";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }
    @PostMapping("/register")
    public String getRegist(@ModelAttribute Client client, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        try {
            MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
            boolean noError= h.Register(client.getUsername(), client.getPassword());
            if(noError) {
                session.setAttribute("logado", true);
                session.setAttribute("cliente", client);
                System.out.println(client.getUsername());
                System.out.println("Registado");
            }else{
                System.out.println("erro no registo");
                model.addAttribute("error", true);
                return "register";
            }
        } catch (RemoteException | NotBoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/search";
    }

    @GetMapping("/logout")
    public String showLogout() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("logado", false);
        session.removeAttribute("cliente");
        return "redirect:/search";
    }

    @GetMapping("/search")
    public String showSearch(@RequestParam(name="show", required=false, defaultValue="false") String name,
                             @RequestParam(name="token", required=false, defaultValue="none") String token,
                             @RequestParam(name="page", required=false, defaultValue="1") String pagina,
                             @RequestParam(name="connected", required=false, defaultValue="-1") String ligados,Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        int page = Integer.parseInt(pagina);
        boolean logado = (boolean) session.getAttribute("logado");
        int logged = 0;
        if(logado==true) logged=1;
        boolean show_results = !Objects.equals(name, "false");
        int show_ligados = Integer.parseInt(ligados);
        String newtoken=token.replace("%20", " ");



        Client client = (Client) session.getAttribute("cliente");
        String nome;
        if (client == null || client.getUsername()==null) nome = "Not logged in";
        else nome = client.getUsername();
        model.addAttribute("username", nome);
        model.addAttribute("logado", session.getAttribute("logado"));
        System.out.println(session.getAttribute("logado"));

        ArrayList<Resultado> novo = new ArrayList<>();
        if(show_results) {
            try {

                MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
                ArrayList<String> linksAssociados = h.FindUrlWithToken(newtoken, logged);
                System.out.println(linksAssociados.size());

                if(linksAssociados!=null) {
                    for (int i = 20 * (page-1); i < linksAssociados.size() && i < 20 * (page); i += 2) {
                        String[] a = linksAssociados.get(i).split("\n\t");

                        String url = ((a[0].split(": "))[1]).strip();
                        System.out.println(url);
                        String titulo = ((a[1].split(": "))[1]).strip();
                        System.out.println(titulo);
                        String citacao = ((a[2].split(": "))[1]).strip();
                        System.out.println(citacao);
                        if (show_ligados >= 0) {
                            //adicionar o top 10
                            if (i == show_ligados * 2 + (20 * (page-1))) {
                                String connectados = h.listPagesConnectedtoAnotherPage(url);
                                ArrayList<String> lista = new ArrayList<>(Arrays.asList(connectados.split("Ligação: ")));
                                model.addAttribute("lista", lista);
                                //System.out.println(i + " " + url);
                                //for(String k: lista) System.out.println(k);
                                novo.add(new Resultado(titulo, citacao, url, lista));
                            } else {
                                novo.add(new Resultado(titulo, citacao, url, null));
                            }

                        } else novo.add(new Resultado(titulo, citacao, url, null));

                    }
                }
            } catch (RemoteException | NotBoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (novo.size() == 0) {
            model.addAttribute("results", "Sem resultados");
            model.addAttribute("nextpage",false);
        }
        else {
            model.addAttribute("results", "");
            model.addAttribute("nextpage",true);
        }
        if(page==1) model.addAttribute("previouspage",false);
        else model.addAttribute("previouspage",true);
        model.addAttribute("resultados", novo);
        model.addAttribute("show", show_results);
        model.addAttribute("tokens", newtoken);
        model.addAttribute("tks", token);
        model.addAttribute("page",page);
        model.addAttribute("ligados", show_ligados);
        model.addAttribute("logado", logado);
        return "search";
    }
    @PostMapping("/search")
    public String postSearch(@ModelAttribute TokensParaPesquisa tokens) {
        String toReturn = "redirect:/search?show=true&page=1&token=";
        toReturn+= tokens.getToken();
        return toReturn;
    }


    @GetMapping("/indexing")
    public String newIndexing(Model model) {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);



        Client client = (Client) session.getAttribute("cliente");
        String nome;
        if (client == null || client.getUsername()==null) nome = "Not logged in";
        else nome = client.getUsername();
        System.out.println(session.getAttribute("logado") + "AHHAHAH");
        model.addAttribute("username", nome);
        model.addAttribute("logado", session.getAttribute("logado"));
        return "indexing";
    }
    @PostMapping("/indexing")
    public String getUrl(@ModelAttribute UrlsForQueue url) {
        try {
            MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
            h.SendUrltoQueue(url.getUrl());
        } catch (RemoteException | NotBoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/search";
    }

    @GetMapping("/linked")
    public String checkUrl(@RequestParam(name="show", required=false, defaultValue="false") String name,
                           @RequestParam(name="url", required=false, defaultValue="null") String url, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);



        Client client = (Client) session.getAttribute("cliente");
        String nome;
        if (client == null || client.getUsername()==null) nome = "Not logged in";
        else nome = client.getUsername();
        model.addAttribute("username", nome);
        model.addAttribute("logado", session.getAttribute("logado"));


        boolean show_results = !Objects.equals(name, "false");
        List<String> lista = null;
        if (show_results){
            try {
                MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");
                String result = h.listPagesConnectedtoAnotherPage(url);
                lista = new ArrayList<>(Arrays.asList(result.split("Ligação: ")));
                model.addAttribute("lista", lista);
            } catch (RemoteException | NotBoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (lista == null || lista.size()==0) {
            model.addAttribute("results", "Sem resultados");
        }else{
            model.addAttribute("results", "");
        }
        model.addAttribute("url", url);
        model.addAttribute("show", name);

        return "linkedpages";
    }
    @PostMapping("/linked")
    public String giveUrl(@ModelAttribute UrlsForQueue url) {
        String toReturn = "redirect:/linked?show=true&url=";
        toReturn+= url.getUrl();
        return toReturn;
    }
}
