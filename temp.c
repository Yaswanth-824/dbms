#include<stdio.h>
void foo(int *p){
    printf("%d",*p);
}
int main(){
    int i=1;
    int *p = &i;
    foo((&i)++);
    printf("%d",*p);
}