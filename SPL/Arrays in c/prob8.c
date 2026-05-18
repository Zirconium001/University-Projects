#include<stdio.h>
int main() 
{
    int arr1[3][3];
    int arr2[3][3];
    int i, j;
    printf("Enter the elements of 1st matrice: ");
    for (i=0;i<3;i++) {
        for (j=0;j<3;j++) {
            scanf("%d", &arr1[i][j]);
        }
    }
    printf("Enter the elements of 2nd matrice: ");
    for (i=0;i<3;i++) {
        for (j=0;j<3;j++) {
            scanf("%d", &arr2[i][j]);
        }
    }
    for (i=0;i<3;i++) {
        for (j=0;j<3;j++) {
            arr1[i][j] -= arr2[i][j];
        }
    }
    for (i=0;i<3;i++) {
        for (j=0;j<3;j++) {
            printf("%d ", arr1[i][j]);
        }
        printf("\n");
    }
}
