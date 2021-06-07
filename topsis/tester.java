import java.util.*;
import java.lang.*;
class mobile_Detail
{
   protected int r;
   protected int c;
   protected double[] w; 
   protected int[][] detail;
   protected  int[] ben;
   protected String[] att;
   public mobile_Detail(int r1,int c1,int[][] det,int[] b,String[] a,double[] wt)
   {
         r=r1;
         c=c1;
         detail=new int[r][c];
         ben=new int[c];
         att=new String[c];
         detail=det;
         ben=b;
         att=a;
         w=new double[c];
         w=wt;
   }
   public mobile_Detail()
   {
         r=0;
         c=0;
   }
      public void dis_detail()
    {
     System.out.print("\t\t\t");
     for(int i=0;i<c;i++)
     {
         System.out.print(" \t\t"+att[i]);
     }
      System.out.println();
         for(int i=0;i<r;i++)
         {
             System.out.print("Object "+(i+1)+"\t\t\t");
             for(int j=0;j<c;j++)
             { 
                 System.out.print(detail[i][j]+"\t\t\t\t");
                
             }
             System.out.println();
         }
    }
    
    void input()
    {
      Scanner in=new Scanner(System.in);
      System.out.println("Enter number of attributes in dataset");
      c=in.nextInt();
      System.out.println("Enter attributes");
      att=new String[c];
      for(int i=0;i<c;i++)
       { 
               System.out.print("Attribute "+(i+1)+": ");
                 att[i]=in.next();
       }
      System.out.println("Enter number of objects to be compared ");
      r=in.nextInt();
      detail=new int[r][c];
       for(int i=0;i<r;i++)
       { 
             System.out.println("Enter details of object: "+(i+1));
             for(int j=0;j<c;j++)
            {
                System.out.print(att[j]+" : ");
                detail[i][j]=in.nextInt();
            }
                 
       }
       System.out.println("Enter weights assigned to attributes: ");
       double sum=0.0;
       w=new double[c];
       ben=new int[c];
       for(int i=0;i<c;i++)
       {
             System.out.print(att[i]+" : ");
             w[i]=in.nextDouble();
             sum=sum+w[i];
       }
       for(int i=0;i<c;i++)
       {
             w[i]=w[i]/sum;
       }
         System.out.println("Mention if the attributes are beneficial(1) or non beneficial(0)");
          for(int i=0;i<c;i++)
         {
             System.out.print(att[i]+" : ");
             ben[i]=in.nextInt();
         }




    }
}
class Calc_rank extends mobile_Detail
{
    double[][] normalized_matrix;
     double[] dist_from_best;
     double[] dist_from_worst;
     int[] rank;


    public Calc_rank(int r1,int c1,int[][] det,int[] b,String[] a,double[] wt)
    {
        super(r1, c1, det,b, a,wt);
       normalized_matrix=new double[r][c];
       dist_from_best=new double[r];
       dist_from_worst=new double[r];
       rank=new int[r];

    }
    public Calc_rank()
    {   }
          void normalization()
     {
            normalized_matrix=new double[r][c];

             for(int i=0;i<c;i++)
             {
                double t=0.0;
              
                 for(int j=0;j<r;j++)
                 {
                    
                     t=t+(detail[j][i]*detail[j][i]);
                 }
                double price=Math.sqrt(t);
                 for(int j=0;j<r;j++)
                 {
                    normalized_matrix[j][i]=(detail[j][i]/price)*w[i];
                 }
             }
             
            
       /*  for(int i=0;i<r;i++)
            {
            for(int j=0;j<c;j++)
             { 
                 System.out.print(normalized_matrix[i][j]+"\t\t");
                
             }
             System.out.println();
         }*/
     }
     double max(int i)
     {
       
       double l=normalized_matrix[0][i];
        for(int j=1;j<r;j++)
        {
             if(normalized_matrix[j][i]>l)
             {
               l=normalized_matrix[j][i];
             }
        }
        return l;
     }
     double min(int i)
     {
       double low=normalized_matrix[0][i];
        for(int j=1;j<r;j++)
        {
             if(normalized_matrix[j][i]<low)
             {
               low=normalized_matrix[j][i];
             }
        }
        return low;
     }
     void eucledian()
     {
          double[] ideal_best=new double[c];
          double[] ideal_worst=new double[c];
           dist_from_best=new double[r];
          dist_from_worst=new double[r];

          for(int i=0;i<c;i++)
          {
                if(ben[i]==1)
                {
                    ideal_best[i]=max(i);
                    ideal_worst[i]=min(i);
                }
                else if(ben[i]==0)
                {
                     ideal_best[i]=min(i);
                     ideal_worst[i]=max(i);
                }
          }
          /*
          for(int i=0;i<c;i++)
          {
              System.out.println("best: "+ideal_best[i]+"  worst: "+ideal_worst[i]);
          }
          */
          for(int i=0;i<r;i++)
          {
             dist_from_best[i]=0.0;
             dist_from_worst[i]=0.0;
             for(int j=0;j<c;j++)
             {
                double tb=normalized_matrix[i][j]-ideal_best[j];
                dist_from_best[i]=dist_from_best[i]+(tb*tb); 
                double tw=normalized_matrix[i][j]-ideal_worst[j];
                dist_from_worst[i]=dist_from_worst[i]+(tw*tw);
             }
              dist_from_best[i]=Math.sqrt(dist_from_best[i]);
              dist_from_worst[i]=Math.sqrt(dist_from_worst[i]);

          }
        /*  for(int i=0;i<r;i++)
          {
              System.out.println("best: "+dist_from_best[i]+ " worst: "+dist_from_worst[i]);
          }*/
          
     }
      int max(double[] t,int r)
        {
            int l=0;
              for(int i=1;i<r;i++)
              {
                  if(t[i]>t[l])
                  {l=i;}
              }
              return l;
        }
        

     void assign_rank()
     {
         double[] preference_matrix=new double[r];
         
                rank=new int[r];

         for(int j=0;j<r;j++)
         {
             preference_matrix[j]=(dist_from_worst[j])/(dist_from_best[j]+dist_from_worst[j]);
             rank[j]=-1;
         }
         int ranking=1;
             for(int i=0;i<r;i++)
                {
                     int mind=max(preference_matrix,r);
                     rank[mind]=ranking;
                     ranking++;
                     preference_matrix[mind]=(double)-1;
                }
                 System.out.println( "\n\nRankings are: \nObject   Rank");

          for(int i=0;i<r;i++)
          {
              System.out.println( "Object "+(i+1)+" :"+rank[i]);
          }
          
     }
}

class tester
{
     public static void main(String[] args)
     {
            
            double[] wt={0.33333333,0.33333333,0.3333333333};  
            int[][] det={{7,60,5},{9,60,6},{5,70,9},{4,80,3}}; 
            int[] ben={1,1,0};
            String[] a={"x1","x2","x3"};
            Calc_rank m1= new Calc_rank(4,3,det,ben,a,wt);
            m1.dis_detail();
            m1.normalization();
            m1.eucledian();
            m1.assign_rank();
            Calc_rank m2=new Calc_rank();
            m2.input();
             m2.dis_detail();
            m2.normalization();
            m2.eucledian();
            m2.assign_rank();
     }
}