package com.labs.neko.nekofxmusicplayer;


import com.labs.neko.nekofxmusicplayer.Models.Music;

public class Queue {
   private NodeQueue first;
   private NodeQueue last;

   public Queue(){
   }

   public boolean isEmpty(){
       return first == null & last == null;
   }

   public void pushFirst(Music music){
       NodeQueue node = new NodeQueue(music);
       if(isEmpty()){
           first = last = node;
       }else{
           node.setNext(first);
           first = node;
       }
   }

   public void pushEnd(Music music){
       NodeQueue node = new NodeQueue(music);
       if(isEmpty()){
           first = last = node;
       }else{
           last.setNext(node);
           last = node;
       }
   }



   public void pushNext(Music music){
       NodeQueue node = new NodeQueue(music);
       if(isEmpty()){
           first = node;
           last = node;
       }else{
           if(first.getNext()==last){
               last.setNext(node);
               last = node;
           }else{
               node.setNext(first.getNext());
               first.setNext(node);
           }
       }
   }

   public Music popMusic(){
       if(!isEmpty()){
           Music music = first.getMusic();
           if(first==last){
               first = last = null;
           }else{
               first = first.getNext();
           }
           return music;
       }
       return null;
   }

}
