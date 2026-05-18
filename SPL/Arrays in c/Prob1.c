#include<stdio.h>

int main() 
{
    int arr1[5], arr2[5];
    int i;
    printf("Elements of 1st array: ");
    for (i=0;i<5;i++) {
        scanf("%d", &arr1[i]);
    }
    for (i=0;i<5;i++) {
        arr2[i] = arr1[i];
    }
    printf("Coppied Elements of 2nd array: ");
    for (i=0;i<5;i++) {
        printf("%d ", arr2[i]);
    }

}