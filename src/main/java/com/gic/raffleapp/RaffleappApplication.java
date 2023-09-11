package com.gic.raffleapp;

import com.gic.raffleapp.util.RaffleUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class RaffleappApplication{

	public static void main(String[] args) throws IOException {

		SpringApplication.run(RaffleappApplication.class, args);
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.println("Welcome to My Raffle App");
			System.out.println("Status: "+(RaffleUtil.isDrawStarted()?
					"Draw is ongoing. Raffle pot size is $"+RaffleUtil.getPotSize():"Draw has not been started"));
			System.out.println("[1] Start a New Draw");
			System.out.println("[2] Buy Tickets");
			System.out.println("[3] Run Raffle");
			int option = 0;
			try{
			option = Integer.parseInt(scanner.nextLine());
			}catch(Exception exception){
				System.out.println("invalid option");
				continue;
			}
			System.out.println("input:"+option);
			if(option==1){
				if(RaffleUtil.isDrawStarted()){
					System.out.println("Raffle draw has already been started.press other options");
					continue;
				}
				System.out.println("New Raffle draw has been started. Initial pot size: $"+RaffleUtil.getPotSize());
				RaffleUtil.startRaffleDraw();
				System.out.println("Press 0 to return to main menu");
				scanner.nextLine();
			} else if (option==2) {
				if(!RaffleUtil.isDrawStarted()){
					System.out.println("Raffle draw has not been started.");
					continue;
				}
				System.out.println("Enter your name, no of tickets to purchase");
				String nameWithNoOfTickets = scanner.nextLine();
				String[] arr = nameWithNoOfTickets.split(",");
				if(arr.length!=2){
					System.out.println("invalid input");
					continue;
				}
				String name = arr[0];
				int noOfTickets = 0 ;
				try{
					 noOfTickets = Integer.parseInt(arr[1]);
				}catch (Exception e){
					System.out.println("invalid input");
					continue;
				}
				RaffleUtil.generateTickets(noOfTickets,name);

			}else if(option==3){
				if(!RaffleUtil.isDrawStarted()){
					System.out.println("Raffle draw has not been started");
					continue;
				}
				System.out.println("Running Raffle..");
				RaffleUtil.runRaffle();
			}else{
				System.out.println("Invalid option");
				System.out.println("Press any key to return to main menu");
			}
		}
	}


}
