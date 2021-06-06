/*Promothee-II*/

import java.util.*;

//class to store and display mobile details
class mobile_Detail
{
   protected int r;
   protected int c;
   protected double[] w; 
   protected int[][] detail;
   protected  int[] ben;
   protected String[] att;
   public mobile_Detail(int r1,int c1,double[] wt,int[][] det,int[] b,String[] a)
   {
         r=r1;
         c=c1;
         w=new double[c]; 
         detail=new int[r][c];
         ben=new int[c];
         att=new String[c];
         w=wt;
         detail=det;
         ben=b;
         att=a;
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
             System.out.print("Mobile "+(i+1)+"\t\t\t");
             for(int j=0;j<c;j++)
             { 
                 System.out.print(detail[i][j]+"\t\t\t\t");
                
             }
             System.out.println();
         }
    }
}
//Class to perform calculations and assign rank
class Calculate_rank extends mobile_Detail
{

       Calculate_rank(int r1,int c1,double[] wt,int[][] det,int[] b,String[] a)
       {
           super(r1,c1,wt,det,b,a);
       }
        protected double[][] normalized_matrix=new double[r][c];
        protected double [] max=new double[c];
        protected double[] min= new double[c];
        protected double[][][] difference_matrix=new double [r][r-1][c];
        protected double[][] aggregated= new double[r][r];
        protected double[][] flow=new double[r][3];
        protected int[] rank=new int[r];
        //function to normalize matrix and normalized_matrix will contain the result
        void normalizing()
        {
               for(int i=0;i<c;i++)
               {
                 double low=detail[0][i];
                 double large=detail[0][i];
                 for(int j=1;j<r;j++)
                 {
                     if(detail[j][i] > large)
                     {
                        large=detail[j][i];
                     }
                     else if(detail[j][i]<low)
                     {
                          low=detail[j][i]; 
                     }
                     
                 }
                 max[i]=large;
                 min[i]=low;
               
               }
               for(int i=0;i<c;i++)
               { 
                if(ben[i]==1)
                 {
                  for(int j=0;j<r;j++)
                  {
                    normalized_matrix[j][i]= ((detail[j][i]-min[i])/(max[i]-min[i]));
                  }
                 }
                 else{
                     for(int j=0;j<r;j++)
                  {
                    normalized_matrix[j][i]= (double)((max[i]-detail[j][i])/(max[i]-min[i]));
                  }
                 }
               }
               
               //To Display Normalized matrix
               for(int i=0;i<r;i++)
                {
                    System.out.print("Mobile "+(i+1)+"\t\t");
                    for(int j=0;j<c;j++)
                    {  
                         System.out.print(normalized_matrix[i][j]+"\t\t");
                
                     }
                    System.out.println();
               }
        }
        //Calculating difference matrix and aggregrate matrix
        void difference_matrix()
        {
             for(int i=0;i<r;i++)
             {
               for(int j=0;j<c;j++)
               {
                 for(int l=0,i1=0;l<(r-1);l++,i1++)
                 {
                    if(i1!=i)
                    {
                         difference_matrix[i][l][j]=normalized_matrix[i][j]-normalized_matrix[i1][j];
                    }
                    else
                    {
                        l--;
                    }
                    
                 }
                }
              }
    
              
               for(int i=0;i<r;i++)
             {
               for(int j=0;j<c;j++)
               {
                 for(int l=0;l<(r-1);l++)
                 {
                     if(difference_matrix[i][l][j]<0)
                     {difference_matrix[i][l][j]=0;}
                     difference_matrix[i][l][j]=difference_matrix[i][l][j]*w[j];
                 }
                }

              }
              double tw=0;
              for(int i=0;i<c;i++)
              {
                   tw=w[i]+tw;
              }
              
              double[] t=new double[(r-1)*r];
                       System.out.println(r*c);

              int count=0;
               for(int i=0;i<r;i++)
             {
               for(int j=0;j<(r-1);j++)
               {
                 double rt=0.0;
                 for(int l=0;l<c;l++)
                 {
                     rt=difference_matrix[i][j][l]+rt;                    
                 }
                 System.out.println(count);

                 t[count]=rt;
                 count++;
                 }
              }
              count=0;
                for(int i=0;i<r;i++)
              {
                  for(int j=0;j<r;j++)
                  {
                     if(i==j)
                     {aggregated[i][j]=-1;}
                     else{
                        aggregated[i][j]=t[count];
                        count++;
                     }
                  }

               }
             
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
        
        
        //Calculating entering flow and leaving flow and final rank
        void calc_rank()
        {
              for(int i=0;i<r;i++)
              {
                    double row_sum=0.0,col_sum=0.0;

                for(int j=0;j<r;j++)
                {
                    if(i!=j)
                    {
                    row_sum=row_sum+aggregated[i][j];
                    col_sum=col_sum+aggregated[j][i];
                    }
                }
                flow[i][0]=row_sum/(r-1);
                flow[i][1]=col_sum/(r-1);
               }
               double[] te=new double[r];
               for(int i=0;i<r;i++)
               {
                    flow[i][2]=flow[i][0]-flow[i][1];
                    rank[i]=-1;
                    te[i]=flow[i][2];
                } 
                int ranking=1;
                
                for(int i=0;i<r;i++)
                {
                     int mind=max(te,r);
                     rank[mind]=ranking;
                     ranking++;
                     te[mind]=(double)-1;
                }
                System.out.println("\n\nRankings are: ");
                 
                for(int i=0;i<r;i++)
                {
                     System.out.println("Mobile "+(i+1)+": "+rank[i]);
                }

        }
} 

class tester
{
     public static void main(String[] args)
     {
            double[] wt={0.35,0.25,0.25,0.15};  
            int[][] det={{250,16,12,5},{200,16,8,3},{300,32,16,4},{275,32,8,2},{200,32,16,4}}; 
            int[] ben={0,1,1,1};
            String[] a={"Cost","Storage Space","Camera","Looks"};
            Calculate_rank m1= new Calculate_rank(5,4,wt,det,ben,a);
            m1.dis_detail();
            m1.normalizing();
            m1.difference_matrix();
            m1.calc_rank();
            
     }
}