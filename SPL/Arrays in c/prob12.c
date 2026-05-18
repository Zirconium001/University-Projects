#include<stdio.h>

int main() 
{
    int arr[3][3] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    int row, col, val;

    do {
        printf("Enter row and column of the value: ");
        scanf("%d %d", &row, &col);
    } while (row<0 || row>2 || col<0 || col>2);
    
    
    printf("Enter value: ");
    scanf("%d", &val);

    arr[row][col] = val;

    printf("Updated Matrice\n");
    for (int i=0;i<3;i++) {
        for (int j=0;j<3;j++) {
            printf("%d ", arr[i][j]);
        }
        printf("\n");
    }
}