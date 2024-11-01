#include<stdio.h>
#include<stdlib.h>
void *allocate(size_t bytesize,int maxNoOfele){
    void *ptr = malloc(bytesize*maxNoOfele);
    if(ptr == NULL){
        printf("Allocatipon Failed");
        exit(1);
    }
    return ptr;
}

void deallocate(void *ptr){
    if(ptr != NULL){
        free(ptr);
    }
}

void takeInput(size_t bytesize,int maxNoOfele,void *ptr){
    char*input = (char*)ptr;
    int inputBytes = scanf("%s",input);
    if(inputBytes > (bytesize*maxNoOfele)){
        printf("OverFlow");
        return ;
    }
    printf("Accepted");
}

int main() {
    size_t elementSize;
    int numElements;
    printf("Enter the size of each element (in bytes): ");
    scanf("%zu", &elementSize);

    printf("Enter the number of elements to allocate: ");
    scanf("%d", &numElements);
    void* memory = allocate(elementSize, numElements);
    takeInput(elementSize, numElements,memory);

    deallocate(memory);

    return 0;
}


