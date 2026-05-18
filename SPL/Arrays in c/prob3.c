#include<stdio.h>

int main() 
{
    int arr[5];
    int i;
    int max;
    for (i=0;i<5;i++) {
        scanf("%d", &arr[i]);   
    }
    max = arr[0];
    
    for (i=0;i<5;i++) {
        if (arr[i] > max)
           max = arr[i-1];
        
    }
    printf("2nd largest value: %d\n", max);
    
}