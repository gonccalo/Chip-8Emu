import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Created by gonccalo on 06-07-2016.
 */
public class Cpu {
    private Random rnd = new Random();
    //registos de 8bits
    private short[] reg;
    //registo I de 16 bits
    private int index;
    //memoria de 4096 bytes
    private short[] mem;
    //Program counter
    private int pc;
    //stack pointer
    private short sp;
    //stack
    private int[] stack;
    //screen
    private short screen[][];
    //buzzer timer
    private short bTimer;
    //delay timer
    private short dTimer;
    //keys
    private short[] keys;

    /*
+---------------+= 0xFFF (4095) End of Chip-8 RAM
|               |
|               |
|               |
|               |
|               |
| 0x200 to 0xFFF|
|     Chip-8    |
| Program / Data|
|     Space     |
|               |
|               |
|               |
+- - - - - - - -+= 0x600 (1536) Start of ETI 660 Chip-8 programs
|               |
|               |
|               |
+---------------+= 0x200 (512) Start of most Chip-8 programs
| 0x000 to 0x1FF|
| Reserved for  |
|  interpreter  |
+---------------+= 0x000 (0) Start of Chip-8 RAM
     */
    public void CpuReset(){
        reg = new short[16];
        index = 0;
        pc = 0x200;
        mem = new short[4096];
        stack = new int[16];
        screen = new short[64][32];
        bTimer = 0;
        dTimer = 0;
        keys = new short[16];
    }

    public void releaseKey(short k){
        keys[k]=0;
    }
    public void pressKey(short k){
        keys[k]=1;
    }
    public void decreaseTimers(){
        if(bTimer > 0)
            bTimer--;
        if(dTimer > 0)
            dTimer--;
        if (bTimer > 0)
            playBeep();
        //System.out.println(dTimer);
    }
    private void playBeep(){

    }

    public void loadRom(String filePath){
        //char sprites from 0 to F
        int[] initMem = {0xf0, 0x90, 0x90, 0x90, 0xf0, 0x20, 0x60, 0x20, 0x20, 0x70, 0xf0, 0x10, 0xf0, 0x80, 0xf0, 0xf0, 0x10, 0xf0, 0x10, 0xf0,
                0x90, 0x90, 0xf0, 0x10, 0x10, 0xf0, 0x80, 0xf0, 0x10, 0xf0, 0xf0, 0x80, 0xf0, 0x90, 0xf0, 0xf0, 0x10, 0x20, 0x40, 0x40, 0xf0, 0x90, 0xf0, 0x90, 0xf0, 0xf0,
                0x90, 0xf0, 0x10, 0xf0, 0xf0, 0x90, 0xf0, 0x90, 0x90, 0xe0, 0x90, 0xe0, 0x90, 0xe0, 0xf0, 0x80, 0x80, 0x80, 0xf0, 0xe0, 0x90, 0x90, 0x90, 0xe0, 0xf0, 0x80,
                0xf0, 0x80, 0xf0, 0xf0, 0x80, 0xf0, 0x80, 0x80};
        for (int i = 0; i < initMem.length; i++) {
            mem[i] = (short) (initMem[i] & 0x00FFFF);
        }
        Path fp = Paths.get(filePath);
        byte[] rom = null;
        try {
            rom = Files.readAllBytes(fp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0;i<0xFFF && i<rom.length;i++){
            mem[0x200+i] = rom[i];
        }
    }

    public short[][] getScreen() {
        return screen;
    }

    //opcodes com 2 bytes
    public int getNextOP(){
        int op;
        op = mem[pc] & 0xFF;
        op = op << 8;
        op = op | (0x00FF & mem[pc+1]);
        pc += 2;
        return op;
    }

    public void decodeAndExe(int op){
        switch (op & 0xF000){
            case 0x0000:
                switch (op & 0x000F){
                    case 0x000E:
                        opCode00EE();
                        break;
                    case 0x0000:
                        opCode00E0();
                        break;
                    default:
                        break;
                }
                break;
            case 0x1000:
                //jump
                pc = op & 0x0FFF;
                break;
            case 0x2000:
                opCode2NNN(op & 0x0FFF);
                break;
            case 0x3000:
                opCode3XKK((op & 0x0F00) >> 8, (short)(op & 0x00FF));
                break;
            case 0x4000:
                opCode4XKK((op & 0x0F00) >> 8, (short)(op & 0x00FF));
                break;
            case 0x5000:
                opCode5XY0(op & 0x0F00, op & 0x00F0);
                break;
            case 0x6000:
                opCode6XKK((op & 0x0F00) >> 8, (short)(op & 0x00FF));
                break;
            case 0x7000:
                opCode7XKK((op & 0x0F00) >> 8, (short)(op & 0x00FF));
                break;
            case 0x8000:
                switch (op & 0x000F){
                    case 0x0000:
                        opCode8XY0((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0001:
                        opCode8XY1((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0002:
                        opCode8XY2((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0003:
                        opCode8XY3((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0004:
                        opCode8XY4((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0005:
                        opCode8XY5((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0006:
                        opCode8XY6((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x0007:
                        opCode8XY7((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    case 0x000E:
                        opCode8XYE((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                        break;
                    default:
                        break;
                }
                break;
            case 0x9000:
                opCode9XY0((op & 0x0F00)>>8, (op & 0x00F0)>>4);
                break;
            case 0xA000:
                opCodeANNN(op & 0x0FFF);
                break;
            case 0xB000:
                opCodeBNNN(op & 0x0FFF);
                break;
            case 0xC000:
                opCodeCXKK((op & 0x0F00) >> 8, (short)(op & 0x00FF));
                break;
            case 0xD000:
                opCodeDXYN((op & 0x0F00)>>8, (op & 0x00F0)>>4, op & 0x000F);
                break;
            case 0xE000:
                switch (op & 0x000F){
                    case 0x000E:
                        opCodeEX9E((op & 0x0F00) >> 8);
                        break;
                    case 0x0001:
                        opCodeEXA1((op & 0x0F00) >> 8);
                        break;
                    default:
                        break;
                }
                break;
            case 0xF000:
                switch (op & 0x00FF){
                    case 0x0007:
                        opCodeFX07((op & 0x0F00)>>8);
                        break;
                    case 0x000A:
                        opCodeFX0A((op & 0x0F00)>>8);
                        break;
                    case 0x0015:
                        opCodeFX15((op & 0x0F00)>>8);
                        break;
                    case 0x0018:
                        opCodeFX18((op & 0x0F00)>>8);
                        break;
                    case 0x001E:
                        opCodeFX1E((op & 0x0F00)>>8);
                        break;
                    case 0x0029:
                        opCodeFX29((op & 0x0F00)>>8);
                        break;
                    case 0x0033:
                        opCodeFX33((op & 0x0F00)>>8);
                        break;
                    case 0x0055:
                        opCodeFX55((op & 0x0F00)>>8);
                        break;
                    case 0x0065:
                        opCodeFX65((op & 0x0F00)>>8);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void opCode00EE(){
        pc = stack[sp];
        sp--;
    }

    private void opCode00E0(){
        for(int i = 0; i < 64;i++){
            for(int j=0;j<32;j++){
                screen[i][j] = 0;
            }
        }
    }

    private void opCode2NNN(int p){
        sp++;
        stack[sp] = pc;
        pc = p;
    }

    private void opCode3XKK(int reg, short kk){
        if(this.reg[reg] == kk)
            pc += 2;
    }

    private void opCode4XKK(int reg, short kk){
        if(this.reg[reg] != kk)
            pc += 2;
    }

    private void opCode5XY0(int regx, int regy) {
        if(this.reg[regx] == this.reg[regy])
            pc += 2;
    }

    private void opCode6XKK(int reg, short kk) {
        this.reg[reg] = kk;
    }

    private void opCode7XKK(int reg, short kk) {
        this.reg[reg] += kk;
        this.reg[reg] = (short)(this.reg[reg] & 0x00FF);
    }

    private void opCode8XY0(int x, int y) {
        this.reg[x] = this.reg[y];
    }

    private void opCode8XY1(int x, int y) {
        this.reg[x] |= this.reg[y];
    }

    private void opCode8XY2(int x, int y) {
        this.reg[x] &= this.reg[y];
    }

    private void opCode8XY3(int x, int y) {
        this.reg[x]^= this.reg[y];
    }

    private void opCode8XY4(int x, int y) {
        this.reg[x] += this.reg[y];
        if(this.reg[x] > 255){
            this.reg[15] = 1;
            this.reg[x] = (short) (this.reg[x] & 0x00FF);
        }
        else{
            this.reg[15] = 0;
        }
    }

    private void opCode8XY5(int x, int y) {
        if(this.reg[x] > this.reg[y])
            this.reg[15] = 1;
        else
            this.reg[15] = 0;
        this.reg[x] -= this.reg[y];
        this.reg[x] = (short) (this.reg[x] & 0x00FF);
    }

    private void opCode8XY6(int x, int y) {
        this.reg[15] = (short)(this.reg[x] & 0x1);
        this.reg[x] >>= 1;
    }

    private void opCode8XY7(int x, int y) {
        if(this.reg[y] > this.reg[x])
            this.reg[15] = 1;
        else
            this.reg[15] = 0;
        this.reg[x] = (short)(this.reg[y] - this.reg[x]);
        this.reg[x] = (short) (this.reg[x] & 0x00FF);
    }

    private void opCode8XYE(int x, int y) {
        this.reg[15] = (short)(this.reg[x] >> 7);
        this.reg[x] <<= 1;
        this.reg[x] = (short) (this.reg[x] & 0x00FF);
    }

    private void opCode9XY0(int x, int y) {
        if(this.reg[x] != this.reg[y])
            pc += 2;
    }

    private void opCodeANNN(int nnn) {
        index = nnn;
    }

    private void opCodeBNNN(int nnn) {
        pc += (nnn + this.reg[0]);
    }

    private void opCodeCXKK(int x, short kk) {
        byte[] b = new byte[1];
        rnd.nextBytes(b);
        this.reg[x] = (short)(kk & b[0]);
    }

    private void opCodeDXYN(int x, int y, int n) {
        short[] sprite = new short[n];
        for(int i = 0;i<n;i++){
            sprite[i] = (short)(mem[index+i] & 0x0FF);
        }
        this.reg[15] = 0;
        for(int i = 0;i<n;i++){
            for(int j = 0;j<8;j++){
                //bit = 1 logo tem de ser mudado o px
                if(((sprite[i] >> (7-j)) & 0x1) == 1){
                    if(screen[this.reg[x]+j][this.reg[y]+i] == 1){
                        this.reg[15] = 1;
                        screen[(this.reg[x]+j)&0x03F][(this.reg[y]+i)&0x01F] = 0;
                    }
                    else {
                        screen[(this.reg[x] + j) & 0x03F][(this.reg[y] + i) & 0x01F] = 1;
                    }
                }
            }
        }
    }

    private void opCodeEX9E(int x) {
        if(keys[this.reg[x]] == 1)
            pc+=2;
    }

    private void opCodeEXA1(int x) {
        if(keys[this.reg[x]] != 1)
            pc+=2;
    }

    private void opCodeFX07(int x) {
        this.reg[x] = dTimer;
    }

    private void opCodeFX0A(int x) {
        short pressedKey = -1;
        for(short i=0;i<16;i++){
            if(keys[i] == 1){
                pressedKey = i;
            }
        }
        if (pressedKey == -1)
            pc -= 2;
        else
            this.reg[x] = pressedKey;
    }

    private void opCodeFX15(int x) {
        dTimer = this.reg[x];
    }

    private void opCodeFX18(int x) {
        bTimer = this.reg[x];
    }

    private void opCodeFX1E(int x) {
        index += this.reg[x];
        //so 16 bits
        index = index & 0xFFFF;
    }

    private void opCodeFX29(int x) {
        //estao de 0x000 ate 0x1FF
        //5 bytes cada char
        index = this.reg[x] * 5;
    }

    private void opCodeFX33(int x) {
        mem[index] = (short)(this.reg[x] / 100);
        mem[(index + 1)& 0x0FFFF] = (short)((this.reg[x] / 10) % 10);
        mem[(index + 2)& 0x0FFFF] = (short)(this.reg[x] % 10);
    }

    private void opCodeFX55(int x) {
        for(int i=0;i<=x;i++){
            mem[(index + i)& 0x0FFFF] = this.reg[i];
        }
        index = (index + x + 1) & 0x0FFFF;
    }

    private void opCodeFX65(int x) {
        for (int i=0;i<x;i++){
            this.reg[i] = mem[(index + i)& 0x0FFFF];
        }
        index = (index + x + 1) & 0x0FFFF;
    }
}
