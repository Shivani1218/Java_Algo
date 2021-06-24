/*Promethee-II*/

import java.util.*;
import java.util.concurrent.TimeUnit;

//class to store and display mobile details
class mobile_Detail
{
   protected int r;
   protected int c;
   protected double[] w; 
   protected double[][] detail;
   protected  int[] ben;
   protected String[] att;
   public mobile_Detail(int r1,int c1,double[] wt,double[][] det,int[] b,String[] a)
   {
         r=r1;
         c=c1;
         w=new double[c]; 
         detail=new double[r][c];
         ben=new int[c];
         att=new String[c];
         w=wt;
         detail=det;
         ben=b;
         att=a;
   }
   public mobile_Detail(int r1,int c1)
   { r=r1;
   c=c1;}
      public void dis_detail()
    {
     System.out.print("\t\t\t");
     for(int i=0;i<c;i++)
     {
         System.out.print(" \t\t\t\t"+att[i]);
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
    
     void input()
    {
    
            Scanner in=new Scanner(System.in);
      
            System.out.println("Enter attributes");
            att=new String[c];
            for(int i=0;i<c;i++)
            { 
               System.out.print("Attribute "+(i+1)+": ");
                 att[i]=in.next();
             }
              
       
      
      detail=new double[r][c];
       for(int i=0;i<r;i++)
       { 
             System.out.println("Enter details of object: "+(i+1));
             for(int j=0;j<c;j++)
            {
                System.out.print(att[j]+" : ");
                detail[i][j]=in.nextDouble();
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
//Class to perform calculations and assign rank
class Calculate_rank extends mobile_Detail
{

       Calculate_rank(int r1,int c1,double[] wt,double[][] det,int[] b,String[] a)
       {
           super(r1,c1,wt,det,b,a);
       }
       Calculate_rank(int r1,int c1)
       {super(r1,c1); }
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
              /* for(int i=0;i<r;i++)
                {
                    System.out.print("Mobile "+(i+1)+"\t\t");
                    for(int j=0;j<c;j++)
                    {  
                         System.out.print(normalized_matrix[i][j]+"\t\t");
                
                     }
                    System.out.println();
               }*/
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
                     System.out.println("Object "+(i+1)+": "+rank[i]);
                }

        }
} 

class tester_promethee
{

     static int getc()
     {
             int c;
             Scanner in=new Scanner(System.in);
             System.out.println("Enter number of attributes in dataset");
             c=in.nextInt();
             return c;
     }
     static int getr()
     {
            int r;
            Scanner in=new Scanner(System.in);
            System.out.println("Enter number of objects to be compared ");
            r=in.nextInt();
            return r;

     }
     public static void main(String[] args)
     {
            
           //Added time complexity
            /*double[] wt={0.34,0.27,0.22,0.10,0.02,0.04};  
            double[][] det={{0.94388,0.4144,14194,4,0.052,4},{0.22108,0.4144,7050.5,4,0.0584,4},{0.1842,0.4144,981.2,4,0.0496,4},{0.1764,0.3134,981.2,5,0.4,5},{0.8432,0.3134,14194,3,0.0522,4},{0.3223,0.4144,14194,4,0.0544,4},{0.1842,0.3134,981.2,5,0.00554,4},{0.22343,0.4134,981.2,5,0.4,5},{0.4312,0.4144,14000,4,0.054,5},{0.4312,0.4144,981.2,5,0.05922,5}}; 
            int[] ben={0,0,0,1,0,1};
            String[] a={"a1","a2","a3","a3","a4","a5","a6"};
            Calculate_rank m1= new Calculate_rank(10,6,wt,det,ben,a); 
            m1.dis_detail();
            long startTime = System.nanoTime();
            m1.normalizing();
            m1.difference_matrix();
            m1.calc_rank();
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in milliseconds: " + (double)timeElapsed / 1000000);*/
            
            
            int c1=getc();
            int r1=getr();
            Calculate_rank m2= new Calculate_rank(r1,c1);
            m2.input();
            m2.dis_detail();
            
            m2.normalizing();
            m2.difference_matrix();
            m2.calc_rank();
     }
}