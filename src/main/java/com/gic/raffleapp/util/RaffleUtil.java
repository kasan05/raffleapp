package com.gic.raffleapp.util;

import com.gic.raffleapp.PriceGroup;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class RaffleUtil {
    private static Set<String> ticketSet;
    private static Map<Integer,List<String>> ticketNumberOccurrenceMap;
    private static Map<Integer,Map<String,Integer>> priceGroupAgainstWinnersMap;
    private static boolean drawStarted;

    private static double potSize;
    public static double getPotSize() {
        return potSize;
    }

    public static void setPotSize(double potSize) {
        RaffleUtil.potSize = potSize;
    }
    @PostConstruct
    public void init(){
        ticketSet = new HashSet<>();
        ticketNumberOccurrenceMap = new HashMap<Integer,List<String>>();
        priceGroupAgainstWinnersMap = new HashMap<Integer,Map<String,Integer>>();
        drawStarted = false;
        potSize = 100;
    }

    public static void setDrawStarted(boolean drawStarted) {
        RaffleUtil.drawStarted = drawStarted;
    }

    public static boolean isDrawStarted() {
        return drawStarted;
    }
    public static Set<String> getTicketSet() {
        return ticketSet;
    }

    public static Map<Integer, List<String>> getTicketNumberOccurrenceMap() {
        return ticketNumberOccurrenceMap;
    }

    public void  clearTicketSet(){
        ticketSet.clear();
    }

    public static void startRaffleDraw(){
        setDrawStarted(true);
    }
    public static void generateTickets(int noOfTickets,String name){
        Set<Integer> set = new TreeSet<>();
        String ticket = "";
        for(int i=1;i<=noOfTickets;i++){
            while(set.isEmpty() || !ticketSet.add(ticket)){
                set.clear();
                while(set.size()!=5){
                    int randInt = ThreadLocalRandom.current().nextInt(1, 16);
                    set.add(randInt);
                }
                ticket = set.stream().map(String::valueOf).collect(Collectors.joining(" "));
            }
            System.out.println("Ticket "+i+":"+ticket);
            for(Integer k : set){
                List<String> l = ticketNumberOccurrenceMap.getOrDefault(k,new ArrayList<>());
                l.add(name + "," + i);
                ticketNumberOccurrenceMap.put(k,l);
            }
        }
        setPotSize(getPotSize()+noOfTickets*5);
    }

    public static void runRaffle(){
        Set<String> set = new TreeSet<>();
        while(set.size()!=5){
            int randInt = ThreadLocalRandom.current().nextInt(1, 16);
            set.add(String.valueOf(randInt));
        }
        System.out.println("Winning Ticket is "+set.stream().collect(Collectors.joining(" ")));
        List<String> winnerList = new ArrayList<>();
        Map<String,Integer>  tempWinningTicketCountMap = new HashMap<>();
        for(String k: set){
            List<String> list = ticketNumberOccurrenceMap.get(Integer.parseInt(k));
            if(list !=null){
                winnerList.addAll(list);
            }
        }
        for(String key:winnerList){
            tempWinningTicketCountMap.put(key,tempWinningTicketCountMap.getOrDefault(key,0)+1);
        }
        for(String key : tempWinningTicketCountMap.keySet()){
            String[] a = key.split(",");

            Map<String,Integer> tempMap = priceGroupAgainstWinnersMap.getOrDefault(tempWinningTicketCountMap.get(key),new HashMap<>());
            tempMap.put(a[0],tempMap.getOrDefault(a[0],0)+1);
            priceGroupAgainstWinnersMap.put(tempWinningTicketCountMap.get(key),tempMap);
        }

        printWinnersAgainstPriceGroup();
        clearStorage();
        setDrawStarted(false);
    }

    private static void clearStorage(){
        ticketSet.clear();
        ticketNumberOccurrenceMap.clear();
        priceGroupAgainstWinnersMap.clear();
    }
    private static void printWinnersAgainstPriceGroup(){
        double remainingMoney = 0;
        for(PriceGroup priceGroup: PriceGroup.values()){
            Map<String,Integer> tempMap = priceGroupAgainstWinnersMap.get(priceGroup.getWinningNumbers());
            System.out.println("Group " +priceGroup.getWinningNumbers()+" Winners:");
            if(tempMap==null){
                System.out.println("Nil");
            }else{
                double y = (getPotSize()*priceGroup.getRewardPercentage())/100;
                for(String x:tempMap.keySet()){
                    double ticketValue = y*tempMap.get(x);
                    remainingMoney= remainingMoney+ticketValue;
                    System.out.println(x+" with "+tempMap.get(x) +"winning ticket(s)-"+ticketValue);
                }
            }
        }
        setPotSize(100+remainingMoney);
    }
}
