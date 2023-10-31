import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;

public class BlackJack
{
    public static final String ANSI_RESET = "\u001B[0m";
    
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void print(String info) 
    {
        System.out.print(info);
    }

    public static void pula_linha(int vezes) 
    {
        for (int i = 0; i < vezes; i++)
            print("\n");
    }
    
    public static void AdicionarPontosComAtraso(String input, String cor) 
	{
		final String ANSI_RESET = "\u001B[0m";
		
        System.out.print(cor + "\n" + input + ANSI_RESET);

        try 
        {
            for (int i = 0; i<3; i++) 
            {
                Thread.sleep(700); 
                System.out.print(cor + "." + ANSI_RESET);
            }
        } catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt(); 
        }
    }
    
    public static boolean tdsparam(Boolean[] vetor) 
    {
        for (Boolean esc: vetor) {
            if (esc != null && esc) 
            {
                return false; 
            }
        }
        return true; 
    }

    public static int calcularSomaCartas(ArrayList<String> cartas) {
        int soma = 0;
        int numAces = 0; // Contador de Ases (que podem valer 1 ou 11)

        for (String carta : cartas) {
            String valorCarta = carta.split(" ")[0]; // Obtém o valor da carta (por exemplo, "2", "Ás", "Rei", etc.)

            if (valorCarta.equals("Ás")) {
                numAces++;
                soma += 11; // Assume que o Ás vale 11 inicialmente
            } else if (valorCarta.equals("K") || valorCarta.equals("Q") || valorCarta.equals("J")) {
                soma += 10; // Cartas de figura valem 10
            } else {
                try {
                    soma += Integer.parseInt(valorCarta); // Converte o valor da carta para um inteiro e adiciona à soma
                } catch (NumberFormatException e) {
                    // Ignora cartas que não podem ser convertidas para inteiros
                }
            }
        }

        // Verifica se é necessário reduzir o valor dos Ases de 11 para 1 para evitar estouro
        while (soma > 21 && numAces > 0) {
            soma -= 10; // Reduz o valor do Ás de 11 para 1
            numAces--;
        }

        return soma;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    public static void main(String[] args) throws InterruptedException 
    {
        Scanner ler = new Scanner(System.in);
        DeckOfCards deck = new DeckOfCards();
        Card carta = null;

        int qntdjog = 0;
        int rodada = 1;
        int esc = 0;

        String emb = "EMBARALHANDO";
		String pede = "PEDINDO NOVA(S) CARTA(S)";

        System.out.printf(ANSI_YELLOW + "\t\t\tBLACKJACK" + ANSI_RESET);
        do 
        {
            System.out.print("\n[1] JOGUE CONTRA O DEALER");
            System.out.print("\n[2] JOGUE CONTRA OUTROS JOGADORES");
            System.out.print("\n>> ");
            esc = ler.nextInt();

            if (esc!=1 && esc!=2) 
            {
                print(ANSI_RED + "Número escolhido inválido!" + ANSI_RESET);
            }
        } while (esc!=1 && esc!=2);

        if (esc == 1) 
        {
            do 
            {
                System.out.print("\nQuantos jogadores irão participar da rodada? (min. 1 e máx. 5)");
                System.out.print("\n>> ");
                qntdjog = ler.nextInt();

                if (qntdjog<1 || qntdjog>5) 
                {
                    print(ANSI_RED + "Número de jogadores inválido!" + ANSI_RESET);
                }
            } while (qntdjog<1 || qntdjog>5);
        } else if (esc == 2) 
        {
            do 
            {
                System.out.print("\nQuantos jogadores irão participar da rodada? (min. 2 e máx. 5)");
                System.out.print("\n>> ");
                qntdjog = ler.nextInt();

                if (qntdjog<2 || qntdjog>5) 
                {
                    print(ANSI_RED + "Número de jogadores inválido!" + ANSI_RESET);
                }
            } while (qntdjog<2 || qntdjog>5);
        }

        String[] nomesjog = new String[qntdjog];
        ArrayList<String> jogs = new ArrayList<>();
        ArrayList<String> dealer = new ArrayList<>();

        pula_linha(1);

        for (int i=0; i<qntdjog; i++) 
        {
            System.out.printf("NOME DO JOGADOR %d: ", i + 1);
            nomesjog[i] = ler.next();
        }
        
        AdicionarPontosComAtraso(emb, ANSI_PURPLE);
        deck.shuffle();
        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////       
        if (esc == 1)
        {
        	print(ANSI_CYAN + "\n\nCARTAS DO DEALER" + ANSI_RESET);
			
			if (rodada==1)
			{
				carta = deck.dealCard();
				dealer.add(carta.toString());
			}
			
			carta = deck.dealCard();
			dealer.add(carta.toString());
			
			StringJoiner personal = new StringJoiner(", ", "[", "]");
	        for (String cartap: dealer) 
	        {
	            personal.add(cartap);
	        }
	        
	        System.out.print("\n" + personal.toString());
		}
        
        pula_linha(1);
        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        HashMap<String, ArrayList<String>> Jogadores = new HashMap<>();

        for (int i=0; i<qntdjog; i++) 
        {
            String nomeJogador =  nomesjog[i];
            ArrayList<String> jogador = new ArrayList<>();
            Jogadores.put(nomeJogador, jogador);
        }
              
        print(ANSI_GREEN + "\nCARTAS DOS JOGADORES" + ANSI_RESET);
        for (int i=0; i<qntdjog; i++) 
        {
        	String nomeJogador =  nomesjog[i];

            if (rodada == 1) 
            {
                carta = deck.dealCard();
                Jogadores.get(nomeJogador).add(carta.toString());
                jogs.add(carta.toString());
            }

            carta = deck.dealCard();
            Jogadores.get(nomeJogador).add(carta.toString());
            jogs.add(carta.toString());
            
            print("\n" + nomeJogador.toUpperCase()); 
            System.out.print("\n" + Jogadores.get(nomeJogador) + "\n");
        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String[] escjog = new String[qntdjog];
        Boolean[] pede_para = new Boolean[qntdjog];
        
        for (int i=0; i<pede_para.length; i++) 
        {
            pede_para[i]=true;
        }
                
        while (!tdsparam(pede_para)) 
        {
            for (int i=0; i<qntdjog; i++) 
            {
                System.out.printf("\n%s, deseja outra carta?\nDigite 'pedir' para sim ou 'parar' para não", nomesjog[i].toUpperCase());
                do 
                {
                    System.out.print("\n>> ");
                    escjog[i] = ler.next();

                    if (!escjog[i].equalsIgnoreCase("pedir") && !escjog[i].equalsIgnoreCase("parar")) 
                    {
                        print(ANSI_RED + "Escolha inválida!" + ANSI_RESET);
                    }
                } while (!escjog[i].equalsIgnoreCase("pedir") && !escjog[i].equalsIgnoreCase("parar"));

                if (escjog[i].equals("pedir")) 
                {
                    pede_para[i] = true;
                } else 
                {
                    pede_para[i] = false;
                }
            }

            for (String algmpediu: escjog) 
            {
                if (algmpediu.equalsIgnoreCase("pedir")) 
                {
                    AdicionarPontosComAtraso(pede, ANSI_PURPLE);
                    break;
                }
            }

            for (int i=0; i<qntdjog; i++) 
            {
                String nomeJogador = nomesjog[i];

                if (pede_para[i]) 
                {
                    carta = deck.dealCard();
                    Jogadores.get(nomeJogador).add(carta.toString());
                    jogs.add(carta.toString());
                }
            }

            print(ANSI_GREEN + "\n\nCARTAS DOS JOGADORES" + ANSI_RESET);
            for (int i=0; i<qntdjog; i++) 
            {
            	String nomeJogador = nomesjog[i];

                print("\n" + nomeJogador.toUpperCase());
                System.out.print("\n" + Jogadores.get(nomeJogador) + "\n");
            }
        }
        
     int somaDealer = calcularSomaCartas(dealer);
     int ncartasdealer = 0;
     
     while (somaDealer<17) 
     {
         carta = deck.dealCard();
         dealer.add(carta.toString());
         ncartasdealer++;
         somaDealer = calcularSomaCartas(dealer);
     }
     
     print(ANSI_PURPLE + "\n\n~~~~~~~~~~~~~ RESULTADOS ~~~~~~~~~~~~" + ANSI_RESET);
     if(esc==1)
    	 System.out.println("\nMÃO DO DEALER: " + somaDealer);

     for (int i = 0; i < qntdjog; i++) 
     {
         String nomeJogador = nomesjog[i];
         
         int somaJogador = calcularSomaCartas(Jogadores.get(nomeJogador));

         System.out.println("\nO VALOR DA MÃO DO " + nomeJogador.toUpperCase() + " É " + somaJogador);

         if (somaJogador > 21 || (somaDealer <= 21 && somaDealer >= somaJogador)) 
         {
        	 System.out.printf(ANSI_RED + nomeJogador.toUpperCase() + " PERDEU!" + ANSI_RESET);
         } else 
         {
        	 System.out.printf(ANSI_GREEN + nomeJogador.toUpperCase() + " GANHOU!" + ANSI_RESET);
         }
         pula_linha(1);
     }
        
        ler.close();
    }
}