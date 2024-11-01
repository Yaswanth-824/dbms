#include<bits/stdc++.h>
#define mod 1000000007;
long long find(int k);
using namespace std;
int main(){
    int t;
    cin >> t;
    for(int i=0;i<t;i++){
        int n;
        cin >> n;
        int k; cin >> k;
        int x=0;
        map<int,int> v;
        for(int j=0;j<n;j++){
            cin >> x;
            v[x]++;
        }
        x = 0;
        int ans =0;
        int mans =0;
        for(auto it : v){
            
        }
        
        cout << x << '\n';
    }
}
long long find(int k){
    if(k < 1){
        return 1;
    }
    long long ans = find(k>>1);
    ans = (ans*1LL*ans)%mod;
    if(k&1){
        ans *= 2;
    }
    ans %= mod;
    return ans;
}