#include<stdio.h>

int main() 
{
    int arr[5];
    int i;
    int max, min;
    for (i=0;i<5;i++) {
        scanf("%d", &arr[i]);   
    }
    max = arr[0];
    min = arr[0];
    for (i=0;i<5;i++) {
        if (arr[i] > max)
           max = arr[i];
        if (arr[i] < min) 
           min = arr[i];
    }
    printf("maximum value: %d\n", max);
    printf("minimum value: %d", min);
}