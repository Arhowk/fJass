/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codeeditor;

import java.util.ArrayList;

/**
 *
 * @author Arhowk
 */
public class DSArrayList {
    ArrayList<Integer> orderSort;
    ArrayList<Integer> highestSort;
    
    public DSArrayList(){
        orderSort = new ArrayList();
        highestSort = new ArrayList();
    }
    
    public Integer getHighest(){
        return highestSort.get(0);
    }
    
    public Integer get(int index){
        return orderSort.get(index);
    }
    
    public void remove(Integer i){
        orderSort.remove(i);
        highestSort.remove(i);
    }
    
    
    public void remove(int index){
        Integer i = orderSort.get(index);
        orderSort.remove(i);
        highestSort.remove(i);
    }
    private void addToHighest(Integer val){
        if(val != 0){
            for(int i = 0; i < highestSort.size(); i++){
                if(highestSort.get(i) <= val){
                    highestSort.add(i, val);
                    return;
                }
            }
        }
        highestSort.add(val);
    }
    public void add(Integer val){
        orderSort.add(val);
        addToHighest(val);
    }
    public void add(int index, Integer val){
        orderSort.add(index,val);
        addToHighest(val);
    }
}
