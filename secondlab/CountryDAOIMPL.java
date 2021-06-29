/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secondlab;

/**
 *
 * @author Kimo Store
 */

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CountryDAOIMPL implements CountryDAO{
    List<Country> countries = new LinkedList<>();
        List<City> cities = new LinkedList<>();
    
    @Override
    public List<City> readCitiesFromCSV(String filename) {
		
	String[] splitted;
	File f = new File(filename);		//System.out.println("file exists: "+f.exists());
	if(f.exists()) {
            try {
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line;
		line = br.readLine();	// reads the first line of data
                splitted = line.split(",");
                this.cities.add(createCity(splitted));
		while(line != null) {
			splitted = line.split(",");
			this.cities.add(createCity(splitted));
			line = br.readLine();
		}
		br.close();
		fr.close();
		} catch (IOException e) {
                    e.printStackTrace();
                    }		
        }else {
                System.out.println("file not found!");
            }
        return this.cities;
    }
    
    @Override
    public City createCity(String[] metadata) {
	int id = Integer.parseInt(metadata[0]);
	String name = metadata[1].trim();
	String countryCode = metadata[3].trim();
	int population = Integer.parseInt(metadata[2]);
	return new City(id ,name, population ,countryCode);
    }  
    
    /**
     * Create a map that uses the country code as keys and a list of cities as the value for each country.
     * @return HashMap object.
     */
    @Override
    public Map<String,List<City>> codeCityMap(){
        // create a map to return
        Map<String,List<City>> codeCity = new HashMap<>();
        // start iterator on cites
        Iterator <City> iter =this.cities.iterator();
        /*
        iterate over the list of cites and create a map
        with country codes and cites
        */
        while(iter.hasNext()){
            // take the current city as temp
            City temp = iter.next();
            // take current city code as temp
            String key = temp.getCountrycode();
            /*
            if city code doesnt exist in map: 
            add the city codde and intialize 
            value as list<city> 
            having intial value of current city
            
            if the key exists in map:
            append city to List<City>
            */
            if(!codeCity.containsKey(key)){
                List<City> codeCities = new ArrayList<>();
                codeCities.add(temp);
                codeCity.put(key, codeCities);
            }
            else{
                codeCity.get(key).add(temp);
            }
        }
        return codeCity;      
    }    
    /**
     * This method sorts the list of Cities in ascending order and returns
     * the sorted list.
     * @return list of pyramid
     */
    private List<City> sortPopulationList(List<City> sorted){
        /**
         * apply collections.sort 
         * by passing a lambda expression for 
         * Comparator functional interface implementation.
         */
        Collections.sort(sorted,(City a,City b)-> (int)a.getPopulation()-(int)b.getPopulation());
        return sorted;
    }
    /**
     * For a given country code (from Console) sort the cities according to the population.
     * @param code Country code
     * @return Sorted list
     */
    @Override
    public List<City> getSortedCities(String code){
        // create map of cites and city codes
        Map<String,List<City>> sortedCodeCites = codeCityMap();
        // get Cities for given Country code      
        List<City> unsortedCites = sortedCodeCites.get(code);
        // Sort cites ascendingly by Population
        List<City> sortedCites = sortPopulationList(unsortedCites);
        
        return sortedCites;
        
    }
    /**
     * Highest population city of each country
     * @return Map(string,city)
     */
    @Override
    public Map<String,City> highestPopPerCity(){
        // create a map to return
        Map<String,City> popCity = new HashMap<>();
        // create Country-City map
        Map<String,List<City>> codeCity = codeCityMap();
        // start iterator on cites
        Iterator <Map.Entry<String,List<City>>> iter = codeCity.entrySet().iterator();
        /*
        iterate over the country-city dict
        1-get key
        2-get value
        3-sort value descendingly by population
        4-add to new map the key and highest city
        5-return new map
        */
        
        // try improving with streams.
        while(iter.hasNext()){
            Map.Entry<String,List<City>> entry = iter.next();
            String code = entry.getKey();
            List<City> cities = entry.getValue();
            Collections.sort(cities,Collections.reverseOrder((City a,City b)-> (int)a.getPopulation()-(int)b.getPopulation()));
            
            if(!popCity.containsKey(code)){
                popCity.put(code, cities.get(0));
            }
          
        }
        return popCity;
        
        
    }
      
  
    @Override    
    public List<Country> readCountriesFromCSV(String filename) {

        String[] splitted;
        File f = new File(filename);
        //System.out.println("file exists: "+f.exists());
        if(f.exists()) {
            try {   
                // intialize file reader
                FileReader fr = new FileReader(f);
                // intialize Buffered Reader
                BufferedReader br = new BufferedReader(fr);
                String line;
                line = br.readLine();
                // read 1st line and add it to Countries
                splitted = line.split(",");
                this.countries.add(createCountry(splitted));

                    while(line != null) {
                        splitted = line.split(",");
                        this.countries.add(createCountry(splitted));
                        line = br.readLine();
                    }
                    br.close();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }		
            }else {
                System.out.println("file not found!");
            }
            return this.countries;
        }    


    @Override
    public Country createCountry(String[] metadata) {
            String code = metadata[0];
            String name = metadata[1];
            String continent = metadata[2];
            double surfaceArea = Double.parseDouble(metadata[3]);
            double population = Double.parseDouble(metadata[4]);
            double gnp = Double.parseDouble(metadata[5]);
            int capital = Integer.parseInt(metadata[6]);

            return new Country(code,name,continent,surfaceArea,population,gnp,capital );
    }  


    @Override
    public Map<String, Double> getCountryPop(List<Country> list) {
        Map<String, Double> countryPopMap = new HashMap<>();
        for(Country c: list) {
                countryPopMap.put(c.getName(), c.getPopulation());
            }
            return countryPopMap;
        }


    @Override
    public List <Double> countryPopulationlist(List<Country> list){

        List<Double> population_list = new ArrayList<>();
        double population;
        for (Country c:list){
            population = c.getPopulation();
            population_list.add(population);
        }
        return population_list;
    }

    @Override
    public double avgPopulation(List<Double> list){

      DoubleSummaryStatistics summaryStats = list.stream()
      .mapToDouble((a) -> a)
      .summaryStatistics();
      double avg = summaryStats.getAverage();
     return avg;

    }

    @Override
    public double maxPopulation(List<Double> list){

      DoubleSummaryStatistics summaryStats = list.stream()
      .mapToDouble((a) -> a)
      .summaryStatistics();
      double max = summaryStats.getMax();
     return max;
    }
    
    
    
        @Override
    public City getHighestPopulationCapital(){
        List<City> capitalList = new ArrayList<>();
        for (Country country: countries){
            int countryCapitalId = country.getCapital();
            City foundCity = cities.stream()
                    .filter(city -> city.getId() == countryCapitalId)
                    .findFirst().orElse(null);
            if (foundCity != null) {
                capitalList.add(foundCity);
            }   
        }
        City result = capitalList.stream()
                .max(Comparator.comparing(City::getPopulation)).orElse(null);
        return result;
      
        
    }

}
