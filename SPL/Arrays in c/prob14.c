#include <stdio.h>

int main() {
    int arr[3][3] = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };

    int search;
    int found = 0;
    int i, j;

    printf("Enter the element to search: ");
    scanf("%d", &search);

    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            if (arr[i][j] == search) {
                found = 1;
                printf("Element %d found at position [%d][%d]\n", search, i, j);
                break;  // stop checking after finding once
            }
        }
        if (found)
            break;
    }

    if (!found)
        printf("Element %d not found in the array\n", search);

    return 0;
}

