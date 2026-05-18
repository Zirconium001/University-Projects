#include <stdio.h>
int main() 
{
    int arr1[3][3];
    int arr2[3][3];
    int i, j;
    int equal = 1;

    printf("Enter the elements of 1st matrix: ");
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            scanf("%d", &arr1[i][j]);
        }
    }
    printf("Enter the elements of 2nd matrix: ");
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            scanf("%d", &arr2[i][j]);
        }
    }
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            if (arr1[i][j] != arr2[i][j]) {
                equal = 0;
                break;
            }
        }
        if (!equal) break;
    }
    if (equal)
        printf("Matrices are Equal\n");
    else
        printf("Matrices are Not Equal\n");

    return 0;
}
